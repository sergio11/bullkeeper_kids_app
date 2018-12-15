package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import com.sanchez.sanchez.bullkeeper_kids.core.extension.batch
import com.sanchez.sanchez.bullkeeper_kids.core.extension.empty
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppInstalledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppRuleEnum
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveAppInstalledDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IAppsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
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
        val BATCH_SIZE = 5
    }

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None): Int {

        val kidId = preferenceRepository.getPrefKidIdentity()
        val terminalId = preferenceRepository.getPrefTerminalIdentity()

        // Get Packages
        val packageList =
                systemPackageHelper.getPackages()

        var totalAppsSync = 0

        if(packageList.isNotEmpty()) {

            // Map To App Installed Entity
            val appInstalledList = packageList.mapTo (arrayListOf()) {
                AppInstalledEntity(it.packageName, it.firstInstallTime, it.lastUpdateTime,
                        it.versionName, it.versionCode, it.appName, AppRuleEnum.PER_SCHEDULER.name,
                        it.icon)
            }

            appsInstalledRepository.save(appInstalledList)
            Timber.d("Apps saved -> %d", appInstalledList.size)

            val appSavedList = arrayListOf<AppInstalledEntity>()

            appInstalledList.asSequence().batch(BATCH_SIZE).forEach { group ->

                val response = appsService
                        .saveAppsInstalledInTheTerminal(kidId, terminalId, group.mapTo (arrayListOf()) {
                            SaveAppInstalledDTO(it.packageName, it.firstInstallTime, it.lastUpdateTime,
                                    it.versionName, it.versionCode, it.appName, AppRuleEnum.PER_SCHEDULER.name, kidId,
                                    terminalId, it.icon)
                        })
                        .await()

                response.httpStatus?.let {

                    if(it == "OK") {

                        response.data?.forEach {appDTO ->
                            group.map {
                                if(it.packageName == appDTO.packageName) {
                                    it.serverId = appDTO.identity
                                    it.sync = 1
                                }
                            }
                        }

                        // Save Sync App
                        appsInstalledRepository.save(group)
                        // Add To List
                        appSavedList.addAll(group)
                    } else {
                        Timber.d("No Success Sync SMS")
                    }

                }

            }

            totalAppsSync = appSavedList.size

        }

        return totalAppsSync
    }

}