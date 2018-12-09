package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

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


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None): Int {

        val kidId = preferenceRepository.getPrefKidIdentity()
        val terminalId = preferenceRepository.getPrefTerminalIdentity()

        // Get Packages
        val packageList =
                systemPackageHelper.getPackages()

        // Map To Save App Installed DTOs
        val saveAppInstalledDTOs = packageList.mapTo (arrayListOf()) {
            SaveAppInstalledDTO(String.empty(), it.packageName, it.firstInstallTime, it.lastUpdateTime,
                    it.versionName, it.versionCode, it.appName, AppRuleEnum.PER_SCHEDULER.name, kidId,
                    terminalId)
        }

        Timber.d("Apps to sync -> %s", saveAppInstalledDTOs.size)

        var totalAppsSync = 0

        if(saveAppInstalledDTOs.isNotEmpty()) {

            Timber.d("Sync Apps To Server")
            val response = appsService
                    .saveAppsInstalledInTheTerminal(kidId, terminalId, saveAppInstalledDTOs)
                    .await()

            val appsSaved = response.data?.mapTo(arrayListOf()) {
                AppInstalledEntity(it.identity, it.packageName,
                        it.firstInstallTime, it.lastUpdateTime, it.versionName,
                        it.versionCode, it.appName, it.appRule)
            }

            Timber.d("Save Apps on local storage")
            appsSaved?.let {
                totalAppsSync = appsSaved.size
                appsInstalledRepository.save(it)
            }

        }

        return totalAppsSync
    }

}