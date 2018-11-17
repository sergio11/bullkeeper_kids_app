package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Synchronize Installed Packages Interact
 */
@Singleton
class SynchronizeInstalledPackagesInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val systemPackageHelper: ISystemPackageHelper,
            private val packageInstalledRepository: IPackageInstalledRepository):
        UseCase<Unit, UseCase.None>(retrofit) {


    val TAG = "SYNC_PACKAGES"

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None) {
        val packageList = systemPackageHelper.getPackages()
        Log.d(TAG, "Packages to sync -> ${packageList.size}")
        return packageInstalledRepository.save(packageList)
    }

}