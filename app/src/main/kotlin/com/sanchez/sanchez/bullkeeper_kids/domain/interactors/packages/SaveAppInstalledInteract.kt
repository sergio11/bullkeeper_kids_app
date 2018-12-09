package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
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
            private val appsInstalledRepository: IAppsInstalledRepository):
        UseCase<String, SaveAppInstalledInteract.Params>(retrofit) {


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): String {
        val packageInfo = systemPackageHelper.getPackageInfo(
                params.packageName.replace("package:", ""))
        packageInfo?.let {
            Timber.d("Package Info obtained")
            it.prettyPrint()
            // Save Package
            //appsInstalledRepository.save(it)
            return it.appName
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