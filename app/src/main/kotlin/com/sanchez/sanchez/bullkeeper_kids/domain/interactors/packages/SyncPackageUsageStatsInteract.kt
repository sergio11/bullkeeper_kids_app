package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.extension.batch
import com.sanchez.sanchez.bullkeeper_kids.core.extension.toDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.extension.toDateTimeString
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveAppStatsDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IAppsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageUsageStatsRepository
import com.sanchez.sanchez.bullkeeper_kids.data.entity.SystemPackageUsageStatsEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject


/**
 * Sync Package Usage Stats Interact
 */
class SyncPackageUsageStatsInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val usageStatsService: IUsageStatsService,
            private val packageUsageStatsRepository: IPackageUsageStatsRepository,
            private val appsInstalledRepositoryImpl: IAppsInstalledRepository,
            private val appsService: IAppsService,
            private val preferenceRepository: IPreferenceRepository)
    : UseCase<Int, UseCase.None>(retrofit) {

    val TAG = "SYNC_PACKAGE_USAGE_STATS"

    companion object {
        val BATCH_SIZE = 15
    }



    /**
     * Get Package Stats List
     */
    private fun getPackageStatsList(): List<SystemPackageUsageStatsEntity> {
        // Package Stats List
        val syncPackageStatsList = arrayListOf<SystemPackageUsageStatsEntity>()
        // Get All Apps Installed
        val appsInstalled =
                appsInstalledRepositoryImpl.list().filter { !it.removed }
        if(appsInstalled.isNotEmpty()) {
            // Get Daily Stats
            val usageStatsList =
                    usageStatsService.getDailyStatsFromAYear().distinctBy { it.packageName }
            for (appInstalled in appsInstalled) {
                for (systemPackageStats in usageStatsList) {
                    if (systemPackageStats.packageName == appInstalled.packageName)
                        syncPackageStatsList.add(systemPackageStats)
                }
            }
        }
        return syncPackageStatsList
    }

    /**
     * Get Package Stats To Save
     */
    private fun getPackageStatsToSave(packageStatsEntityList: List<SystemPackageUsageStatsEntity>,
                                      packageStatsEntityListSaved: List<SystemPackageUsageStatsEntity>): List<SystemPackageUsageStatsEntity> {

        // Package Stats To Save
        val packageStatsToSave =
                arrayListOf<SystemPackageUsageStatsEntity>()

        for(packageStats in packageStatsEntityList) {

            var isFound = false
            for(packageStatsSaved in packageStatsEntityListSaved) {
                if(packageStats.packageName == packageStatsSaved.packageName) {
                    isFound = true

                    if(packageStatsSaved.sync == 0
                            || packageStatsSaved.remove == 1 ||
                            packageStats.lastTimeUsed.toDateTime().after(packageStatsSaved.lastTimeUsed.toDateTime()) ){
                        packageStats.serverId = packageStatsSaved.serverId
                        packageStats.sync = packageStatsSaved.sync
                        packageStats.remove = packageStatsSaved.remove
                        packageStatsToSave.add(packageStats)

                    }

                }
            }

            if(!isFound)
                packageStatsToSave.add(packageStats)
        }

        return packageStatsToSave
    }

    /**
     * Get Package Stats To Remove
     */
    private fun getPackageStatsToRemove(
            packageStatsEntityListSaved: List<SystemPackageUsageStatsEntity>,
            packageStatsEntityList: List<SystemPackageUsageStatsEntity>
    ): List<SystemPackageUsageStatsEntity> {

        val packageStatsToRemove = arrayListOf<SystemPackageUsageStatsEntity>()

        for(packageStatsSaved in packageStatsEntityListSaved) {

            if(packageStatsSaved.remove == 1) {
                packageStatsToRemove.add(packageStatsSaved)
                continue
            }

            var isFound = false
            for(packageStats in packageStatsEntityList){
                if(packageStatsSaved.packageName == packageStats.packageName)
                    isFound = true
            }

            if(!isFound) {
                if(packageStatsSaved.sync == 1 && packageStatsSaved.serverId != null) {
                    packageStatsSaved.remove = 1
                    packageStatsToRemove.add(packageStatsSaved)
                } else {
                    packageUsageStatsRepository.delete(packageStatsSaved)
                }
            }

        }

        return packageStatsToRemove
    }

    /**
     * Get Package Stats To Sync
     */
    private fun getPackageStatsToSync(): Pair<List<SystemPackageUsageStatsEntity>, List<SystemPackageUsageStatsEntity>> {
        // Package Stats To Save
        val packageStatsToSave = arrayListOf<SystemPackageUsageStatsEntity>()
        // Package Stats To Remove
        val packageStatsToRemove = arrayListOf<SystemPackageUsageStatsEntity>()
        // Get Package Stats List
        val packageStatsList = getPackageStatsList()
        // Current Package Stats List
        val currentPackageStatsList = packageUsageStatsRepository.list()

         if(packageStatsList.isNotEmpty() && currentPackageStatsList.isEmpty()) {

            // save all stats
             packageStatsToSave.addAll(packageStatsList)

        } else if(packageStatsList.isNotEmpty() && currentPackageStatsList.isNotEmpty()) {

            // Get Package Stats To Save
             packageStatsToSave.addAll(getPackageStatsToSave(packageStatsList, currentPackageStatsList))
            // Get Package Stats To Remove
             packageStatsToRemove.addAll(getPackageStatsToRemove(currentPackageStatsList, packageStatsList))

        }

        return Pair(packageStatsToSave, packageStatsToRemove)

    }

    /**
     * Upload Package Stats
     */
    private suspend fun uploadPackageStats(packageStatsEntityToUpload: List<SystemPackageUsageStatsEntity>): Int {
        Preconditions.checkNotNull(packageStatsEntityToUpload, "Package Stats To Upload")
        Preconditions.checkState(!packageStatsEntityToUpload.isEmpty(), "Package Stats To Upload can not be empty")

        // Get Kid and Terminal
        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()

        val packageStatsUploaded
                = arrayListOf<SystemPackageUsageStatsEntity>()

        packageStatsEntityToUpload.asSequence().batch(BATCH_SIZE).forEach { group ->

            val response = appsService
                    .saveStatsForAllAppsInstalledInTheTerminal(kid, terminal, group.map { SaveAppStatsDTO(
                            it.serverId.orEmpty(),
                            it.firstTimeStamp.toDateTimeString(),
                            it.lastTimeStamp.toDateTimeString(),
                            it.lastTimeUsed.toDateTimeString(),
                            it.totalTimeInForeground, it.packageName, kid, terminal) })
                    .await()

            response.httpStatus?.let {

                if(it == "OK") {

                    response.data?.forEach {appStatsDTO ->
                        group.onEach {
                            if(it.packageName == appStatsDTO.packageName) {
                                Timber.d("SYNC_STATS: Server Id -> %s for package -> %s",
                                        appStatsDTO.identity, it.packageName)
                                it.serverId = appStatsDTO.identity
                                it.sync = 1
                                it.remove = 0

                                Timber.d("SYNC_STATS: Server Id Sync -> %s",
                                         it.serverId)
                            }
                        }
                    }
                    Timber.d("SYNC_STATS: Save Packages")
                    packageUsageStatsRepository.save(group)
                    // Add To List
                    packageStatsUploaded.addAll(group)

                } else {
                    Timber.d("No Success Sync Package USage")
                }

            }
        }

        return packageStatsUploaded.size

    }

    /**
     * Delete Package Stats
     */
    private suspend fun deletePackageStats(packageStatsEntityToRemove: List<SystemPackageUsageStatsEntity>): Int {
        Preconditions.checkNotNull(packageStatsEntityToRemove, "Package Stats to remove can not be null")
        Preconditions.checkState(!packageStatsEntityToRemove.isEmpty(), "Package Stats to remove  can not be empty")

        // Get Kid and Terminal
        val kidId = preferenceRepository.getPrefKidIdentity()
        val terminalId = preferenceRepository.getPrefTerminalIdentity()

        var totalPackageStatsDeleted  = 0

        val response = appsService.deleteAppStats(
                kidId, terminalId, packageStatsEntityToRemove.filter {
                        it.sync == 1 && it.serverId != null }
                .map { it.serverId!! }
        ).await()

        response.httpStatus?.let {

            if (it == "OK") {
                totalPackageStatsDeleted = packageStatsEntityToRemove.size
                // save all as removed = true
                packageStatsEntityToRemove.onEach { it.remove = 1 }
                packageUsageStatsRepository.save(packageStatsEntityToRemove)
                packageUsageStatsRepository.delete(packageStatsEntityToRemove)
            }

        }

        return totalPackageStatsDeleted
    }

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None): Int {
        // Get Package Stats To Sync
        val packageStatsToSync
                = getPackageStatsToSync()
        // Package Stats To Upload
        val packageStatsToUpload = packageStatsToSync.first
        // Package Stats To Remove
        val packageStatsToRemove = packageStatsToSync.second

        var totalPackageStatsSync = 0

        // Package Stats To Upload
        if(packageStatsToUpload.isNotEmpty()) {
            // Save Package Stats
            packageUsageStatsRepository.save(packageStatsToUpload)
            Timber.d("%s Package Stats To Sync -> %s", TAG, packageStatsToUpload.size)
            // Upload Package Stats
            totalPackageStatsSync += uploadPackageStats(packageStatsToUpload)
        }

        // Package Stats To Remove
        if(packageStatsToRemove.isNotEmpty()) {
            // Save Package Stats
            packageUsageStatsRepository.save(packageStatsToRemove)
            Timber.d("%s Package Stats To Remove -> %s", TAG, packageStatsToRemove.size)
            totalPackageStatsSync += deletePackageStats(packageStatsToRemove)
        }

        return totalPackageStatsSync
    }

}