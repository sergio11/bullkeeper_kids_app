package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageInfo
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Get Blocked Packages Interact
 */
@Singleton
class GetBlockedPackagesInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val appsInstalledRepository: IAppsInstalledRepository): UseCase<List<SystemPackageInfo>, UseCase.None>(retrofit) {

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None): List<SystemPackageInfo>
        =  arrayListOf()//appsInstalledRepository.getBlockedPackages()

}