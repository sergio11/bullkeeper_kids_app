package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import android.content.Context
import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import retrofit2.Retrofit
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
            private val packageInstalledRepository: IPackageInstalledRepository,
            private val context: Context): UseCase<String, RemoveInstalledPackageInteract.Params>(retrofit) {


    val TAG = "REMOVE_PACKAGE"

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): String {
        // Find Package by name
        val packageInfo = packageInstalledRepository.findByPackageName(
                packageName = params.packageName.replace("package:", ""))

        packageInfo?.let {
            Log.d(TAG, "Package Info obtained")
            it.prettyPrint()
            // Save Package
            packageInstalledRepository.delete(it)
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