package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IAppsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Remove Installed Package Interact
 */
@Singleton
class RemoveInstalledPackageInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val appsInstalledRepository: IAppsInstalledRepository,
            private val preferenceRepository: IPreferenceRepository,
            private val appService: IAppsService): UseCase<Unit, RemoveInstalledPackageInteract.Params>(retrofit) {
    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params) {
        // Find Package by name
        val appInstalled = appsInstalledRepository.findByPackageName(
                packageName = params.packageName.replace("package:", ""))

        if(appInstalled != null && appInstalled.isValid) {

            Timber.d("Check Server Id ")
            if (!appInstalled.serverId.isNullOrEmpty()) {
                Timber.d("Remove App with server id -> ${appInstalled.serverId}")
                appInstalled.removed = true
                val kid = preferenceRepository.getPrefKidIdentity()
                val terminal = preferenceRepository.getPrefTerminalIdentity()
                // Delete App
                val response = appService
                        .deleteAppInstalledById(kid, terminal, appInstalled.serverId!!)
                        .await()
                response.httpStatus?.let {
                    if(it == "OK") {
                        appsInstalledRepository.delete(appInstalled)
                    }
                }
            } else {
                Timber.d("No server id -> ${appInstalled.serverId}, remove directly")
                appsInstalledRepository.delete(appInstalled)
            }

        }

        Timber.d("Check App -> ${appInstalled?.packageName}")
    }

    /**
     * Params
     */
    data class Params(val packageName: String)
}