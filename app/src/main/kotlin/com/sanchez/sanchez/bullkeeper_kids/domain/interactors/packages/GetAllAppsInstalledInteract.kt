package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages


import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppInstalledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Get All Apps Installed Interact
 */
@Singleton
class GetAllAppsInstalledInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val appsInstalledRepository: IAppsInstalledRepository): UseCase<List<AppInstalledEntity>, UseCase.None>(retrofit) {

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None): List<AppInstalledEntity>  =
            appsInstalledRepository.list()

}