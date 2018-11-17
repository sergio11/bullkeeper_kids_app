package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Save Installed Package Interact
 */
@Singleton
class SaveInstalledPackageInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val systemPackageHelper: ISystemPackageHelper,
            private val packageInstalledRepository: IPackageInstalledRepository):
        UseCase<String, SaveInstalledPackageInteract.Params>(retrofit) {



    val TAG = "SAVE_PACKAGE"

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): String {
        val packageInfo = systemPackageHelper.getPackageInfo(
                params.packageName.replace("package:", ""))
        packageInfo?.let {
            Log.d(TAG, "Package Info obtained")
            it.prettyPrint()
            // Save Package
            packageInstalledRepository.save(it)
            return it.appName
        } ?: run {
            Log.d(TAG, "Package not founded")
            return ""
        }
    }


    /**
     * Interact SocialToken
     */
    data class Params(val packageName: String)

}