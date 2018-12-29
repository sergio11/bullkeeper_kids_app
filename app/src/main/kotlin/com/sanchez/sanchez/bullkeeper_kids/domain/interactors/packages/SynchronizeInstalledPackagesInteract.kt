package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.extension.batch
import com.sanchez.sanchez.bullkeeper_kids.core.extension.toDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppInstalledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppRuleEnum
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveAppInstalledDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IAppsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import org.jsoup.Jsoup
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject


/**
 * Synchronize Installed Packages Interact
 *
 * */
class SynchronizeInstalledPackagesInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val systemPackageHelper: ISystemPackageHelper,
            private val appsInstalledRepository: IAppsInstalledRepository,
            private val appsService: IAppsService,
            private val preferenceRepository: IPreferenceRepository):
        UseCase<Int, UseCase.None>(retrofit) {


    companion object {

        const val BATCH_SIZE = 5
        const val GOOGLE_URL = "https://play.google.com/store/apps/details?id="
        const val ERROR = "error"
    }

    /**
     * Get Apps To Save
     */
    private fun getAppsToSave(
            appsRegisteredInTheTerminal: List<AppInstalledEntity>,
            appsSaved: List<AppInstalledEntity>
            ): List<AppInstalledEntity> {

        val appsToSave = arrayListOf<AppInstalledEntity>()

        for(appRegistered in appsRegisteredInTheTerminal) {
            var isFound = false
            for(appSaved in appsSaved) {
                if(appRegistered.packageName ==
                        appSaved.packageName) {
                    isFound = true

                    val appRegisteredLastUpdate = appRegistered.lastUpdateTime?.toDateTime()
                    val appSavedLastUpdate = appSaved.lastUpdateTime?.toDateTime()

                    if (appRegisteredLastUpdate != null && appSavedLastUpdate != null &&
                            appRegisteredLastUpdate.after(appSavedLastUpdate)) {

                        appRegistered.serverId = appSaved.serverId
                        appRegistered.sync = 0
                        appRegistered.removed = false
                        appsToSave.add(appRegistered)
                    } else {
                        // Is app Save Sync?
                        if (appSaved.sync == 0 || appSaved.removed) {
                            appSaved.removed = false
                            appsToSave.add(appSaved)
                        }
                    }
                }

            }

            if(!isFound)
                appsToSave.add(appRegistered)
        }


        return appsToSave
    }

    /**
     * Get Category
     */
    private fun getCategory(query_url: String): String {
        return try {
            val doc = Jsoup.connect(query_url).get()
            val link = doc.select("a[class=\"hrTbp R8zArc\"]")
            return if(link.text().isNullOrEmpty()) ERROR else link.text()
        } catch (e: Exception) {
            ERROR
        }
    }

    /**
     * Get Apps To Remove
     */
    private fun getAppsToRemove(
            appsSaved: List<AppInstalledEntity>,
            appsRegisteredInTheTerminal: List<AppInstalledEntity>
    ): List<AppInstalledEntity> {

        val appsToRemove = arrayListOf<AppInstalledEntity>()

        for(appSaved in appsSaved) {

            if(appSaved.removed) {
                appsToRemove.add(appSaved)
                continue
            }

            var isFound = false
            for(appRegistered in appsRegisteredInTheTerminal) {
                if(appSaved.packageName == appRegistered.packageName)
                    isFound = true
            }

            if(!isFound) {
                if (appSaved.sync == 1 && appSaved.serverId != null) {
                    appSaved.removed = true
                    appsToRemove.add(appSaved)
                } else {
                    appsInstalledRepository.delete(appSaved)
                }
            }
        }

        return appsToRemove
    }

    /**
     * Get Apps To Sync
     */
    private fun getAppsToSynchronize(): Pair<List<AppInstalledEntity>, List<AppInstalledEntity>> {

        // Get Packages
        val appsRegisteredInTheTerminal =
                systemPackageHelper.getPackages().mapTo (arrayListOf()) {
            AppInstalledEntity(it.packageName, it.firstInstallTime, it.lastUpdateTime,
                    it.versionName, it.versionCode, it.appName, AppRuleEnum.PER_SCHEDULER.name,
                    it.icon)}

        // Apps Saved
        val appsSaved = appsInstalledRepository.list()

        // Apps To Save
        val appsToSave = arrayListOf<AppInstalledEntity>()
        // Apps To Remove
        val appsToRemove = arrayListOf<AppInstalledEntity>()

        if(appsRegisteredInTheTerminal.isEmpty() && appsSaved.isNotEmpty()) {

            // Delete all sync apps
            appsToRemove.addAll(appsSaved
                    .filter { it.sync == 1 && it.serverId != null }
                    .onEach { it.removed = true }
                    .map { it })

            // Delete all unsync apps
            appsInstalledRepository.delete(appsSaved
                    .filter { it.sync == 0 }
                    .map { it })

        } else if(appsRegisteredInTheTerminal.isNotEmpty() && appsSaved.isEmpty()) {

            // save all apps registered
            appsToSave.addAll(appsRegisteredInTheTerminal)

        } else if(appsRegisteredInTheTerminal.isNotEmpty() && appsSaved.isNotEmpty()) {
            // Get Apps to save
            appsToSave.addAll(getAppsToSave(appsRegisteredInTheTerminal, appsSaved))
            // Get apps to remove
            appsToRemove.addAll(getAppsToRemove(appsSaved, appsRegisteredInTheTerminal))
        }

        return Pair(appsToSave, appsToRemove)
    }

    /**
     * Upload Apps To Sync
     */
    private suspend fun uploadAppsToSync(appsToUpload: List<AppInstalledEntity>): Int {
        Preconditions.checkNotNull(appsToUpload, "Apps To Upload can not be null")
        Preconditions.checkState(!appsToUpload.isEmpty(), "App to upload can not be empty")

        val kidId = preferenceRepository.getPrefKidIdentity()
        val terminalId = preferenceRepository.getPrefTerminalIdentity()

        val appsUploadedList = arrayListOf<AppInstalledEntity>()
        /**
         * .onEach {
        it.category = getCategory(GOOGLE_URL + it.packageName)
        }
         */
        appsToUpload.asSequence().batch(BATCH_SIZE).forEach { group ->

            val response = appsService
                    .saveAppsInstalledInTheTerminal(kidId, terminalId, group.mapTo (arrayListOf()) {
                        SaveAppInstalledDTO(it.packageName, it.firstInstallTime, it.lastUpdateTime,
                                it.versionName, it.versionCode, it.appName, AppRuleEnum.PER_SCHEDULER.name, kidId,
                                terminalId, it.icon, it.category)
                    })
                    .await()

            response.httpStatus?.let {

                if(it == "OK") {

                    response.data?.forEach {appDTO ->
                        group.map {
                            if(it.packageName == appDTO.packageName) {
                                it.serverId = appDTO.identity
                                it.sync = 1
                                it.category = appDTO.category
                                it.removed = false
                            }
                        }
                    }

                    // Save Sync App
                    appsInstalledRepository.save(group)
                    // Add To List
                    appsUploadedList.addAll(group)
                } else {
                    Timber.d("No Success Sync SMS")
                }

            }

        }

        return appsUploadedList.size
    }

    /**
     * Remove Apps
     */
    private suspend fun removeApps(appsToRemove: List<AppInstalledEntity>): Int {
        Preconditions.checkNotNull(appsToRemove, "Apps To Remove can not be null")
        Preconditions.checkState(!appsToRemove.isEmpty(), "App to Remove can not be empty")

        val kidId = preferenceRepository.getPrefKidIdentity()
        val terminalId = preferenceRepository.getPrefTerminalIdentity()

        var totalAppsDeleted  = 0

        // Delete Apps Installed
        val response = appsService.deleteAppsInstalled(
                kidId, terminalId, appsToRemove.filter { it.sync == 1 && it.serverId != null }
                    .map { it.serverId!! }
        ).await()

        response.httpStatus?.let {

            if (it == "OK") {
                totalAppsDeleted = appsToRemove.size
                // save all as removed = true
                appsToRemove.onEach { it.removed = true }
                appsInstalledRepository.save(appsToRemove)
                appsInstalledRepository.delete(appsToRemove)
            }

        }

        return totalAppsDeleted

    }

    /**
     * Sync App Rules
     */
    private suspend fun syncAppRules(){

        Timber.d("SYNC_APPS: Sync App Rules")

        val kidId = preferenceRepository.getPrefKidIdentity()
        val terminalId = preferenceRepository.getPrefTerminalIdentity()

        // Get App Rules
        val response = appsService
                .getAppRulesForAppsInTheTerminal(kidId, terminalId).await()

        response.httpStatus?.let {
            if (it == "OK") {

                response.data?.let { appRulesList ->

                    // Get Packages
                    val appsInstalled = appsInstalledRepository.findByPackageNameIn(appRulesList
                            .filter { !it.packageName.isNullOrEmpty()  }.map { it.packageName!! }
                            .toTypedArray())

                    Timber.d("SYNC_APPS: Apps Installed to update -> ${appsInstalled.size}")

                    appsInstalledRepository.save(appsInstalled.onEach {appRulesList.find { appRuleDTO ->  appRuleDTO.packageName == it.packageName }
                            ?.let {appRuleDTO -> it.appRule = appRuleDTO.appRule }})

                }
            }
        }

    }


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None): Int {

        val appsToSync = getAppsToSynchronize()
        // Apps To Save
        val appsToSave = appsToSync.first
        // Apps To Remove
        val appsToRemove = appsToSync.second

        var totalAppsSync = 0

        // Apps To Save
        if(appsToSave.isNotEmpty()) {

            appsInstalledRepository.save(appsToSave)
            Timber.d("Apps to upload -> %d", appsToSave.size)
            totalAppsSync += uploadAppsToSync(appsToSave)

        }

        // Apps To Remove
        if(appsToRemove.isNotEmpty()) {
            appsInstalledRepository.save(appsToRemove)
            Timber.d("Apps to remove -> %d", appsToRemove.size)
            totalAppsSync += removeApps(appsToRemove)
        }


        // Sync App Rules
        syncAppRules()

        return totalAppsSync
    }

}