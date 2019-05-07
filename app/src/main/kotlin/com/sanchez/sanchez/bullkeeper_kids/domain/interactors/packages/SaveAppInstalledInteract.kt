package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppInstalledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveAppInstalledDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IAppsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Save App Installed Interact
 */
@Singleton
class SaveAppInstalledInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val systemPackageHelper: ISystemPackageHelper,
            private val appsInstalledRepository: IAppsInstalledRepository,
            private val preferenceRepository: IPreferenceRepository,
            private val appService: IAppsService):
        UseCase<Unit, SaveAppInstalledInteract.Params>(retrofit) {

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): Unit {

        // Search Package Information
        val packageInfo = systemPackageHelper.getPackageInfo(
                params.packageName.replace("package:", ""))

        packageInfo?.let {packageAdded ->
            Timber.d("Package Info obtained")
            packageAdded.prettyPrint()
            // Map To App Installed Entity
            val appInstalledToSave = AppInstalledEntity()
            appInstalledToSave.appName = packageAdded.appName
            appInstalledToSave.firstInstallTime = packageAdded.firstInstallTime
            appInstalledToSave.icon = packageAdded.icon
            appInstalledToSave.lastUpdateTime = packageAdded.lastUpdateTime
            appInstalledToSave.packageName = packageAdded.packageName
            appInstalledToSave.versionCode = packageAdded.versionCode
            appInstalledToSave.versionName = packageAdded.versionName
            // Save App Installed
            appsInstalledRepository.save(appInstalledToSave)

            val kid = preferenceRepository.getPrefKidIdentity()
            val terminal = preferenceRepository.getPrefTerminalIdentity()

            val saveAppInstalledDTO = SaveAppInstalledDTO()
            saveAppInstalledDTO.terminalId = terminal
            saveAppInstalledDTO.kid = kid
            saveAppInstalledDTO.appRule = appInstalledToSave.appRule
            saveAppInstalledDTO.versionName = appInstalledToSave.versionName
            saveAppInstalledDTO.versionCode = appInstalledToSave.versionCode
            saveAppInstalledDTO.firstInstallTime = appInstalledToSave.firstInstallTime
            saveAppInstalledDTO.lastUpdateTime = appInstalledToSave.lastUpdateTime
            saveAppInstalledDTO.packageName = appInstalledToSave.packageName
            saveAppInstalledDTO.appName = appInstalledToSave.appName
            saveAppInstalledDTO.icon = appInstalledToSave.icon
            // Add App Installed
            val response = appService
                    .addAppInstalled(kid, terminal, saveAppInstalledDTO).await()

            response.httpStatus?.let {

                if(it == "OK") {
                    response.data?.let {appDTO ->
                        val appInstalledSaved = appsInstalledRepository.findByPackageName(appDTO.packageName!!)
                        appInstalledSaved?.let {appSaved ->
                            appSaved.serverId = appDTO.identity
                            appSaved.appRule = appDTO.appRule
                            appSaved.sync = 1
                            appSaved.removed = false
                            appsInstalledRepository.save(appSaved)
                        }
                    }
                }
            }
        }
    }


    /**
     * Params
     */
    data class Params(val packageName: String)

}