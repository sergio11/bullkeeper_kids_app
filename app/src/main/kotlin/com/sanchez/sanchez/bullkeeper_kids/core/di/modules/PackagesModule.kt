package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageUsageStatsRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.GetAllPackagesInstalledInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.GetBlockedPackagesInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.SynPackageUsageStatsInteract
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Packages Module
 */
@Module(includes = [ GlobalServiceModule::class, PersistenceModule::class ])
class PackagesModule {

    /**
     * Provide Get Blocked Packages Interact
     */
    @Provides
    @Singleton
    fun provideGetBlockedPackagesInteract(retrofit: Retrofit, packageInstalledRepository: IPackageInstalledRepository):
            GetBlockedPackagesInteract = GetBlockedPackagesInteract(retrofit, packageInstalledRepository)

    /**
     * Provide Get All Packages Installed Interact
     */
    @Provides
    @Singleton
    fun provideGetAllPackagesInstalledInteract(retrofit: Retrofit, packageInstalledRepository: IPackageInstalledRepository):
            GetAllPackagesInstalledInteract = GetAllPackagesInstalledInteract(retrofit, packageInstalledRepository)


    /**
     * Provide Sync Package Usage Stats Interact
     */
    @Provides
    @Singleton
    fun provideSynPackageUsageStatsInteract(retrofit: Retrofit, usageStatsService: IUsageStatsService, packageUsageStatsRepository: IPackageUsageStatsRepository):
            SynPackageUsageStatsInteract = SynPackageUsageStatsInteract(retrofit, usageStatsService,
                        packageUsageStatsRepository)
}