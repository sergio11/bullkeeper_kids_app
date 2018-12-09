package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
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
            private val systemPackageHelper: ISystemPackageHelper,
            private val appsInstalledRepository: IAppsInstalledRepository,
            private val context: Context): UseCase<String, RemoveInstalledPackageInteract.Params>(retrofit) {
    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): String {
        // Find Package by name
        val appInstalled = appsInstalledRepository.findByPackageName(
                packageName = params.packageName.replace("package:", ""))

        appInstalled?.let {
            Timber.d( "Package Info obtained")
            // Save Package
            appsInstalledRepository.delete(it)
            return it.appName.orEmpty()
        } ?: run {
            Timber.d("Package not founded")
            return ""
        }
    }

    /**
     * Interact SocialToken
     */
    data class Params(val packageName: String)
}