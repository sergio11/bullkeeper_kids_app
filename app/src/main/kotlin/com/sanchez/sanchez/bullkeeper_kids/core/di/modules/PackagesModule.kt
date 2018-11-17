package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageUsageStatsRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.GetAllPackagesInstalledInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.GetBlockedPackagesInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.SynPackageUsageStatsInteract
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import dagger.Module
import dagger.Provides
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
    fun provideGetBlockedPackagesInteract(packageInstalledRepository: IPackageInstalledRepository):
            GetBlockedPackagesInteract = GetBlockedPackagesInteract(packageInstalledRepository)

    /**
     * Provide Get All Packages Installed Interact
     */
    @Provides
    @Singleton
    fun provideGetAllPackagesInstalledInteract(packageInstalledRepository: IPackageInstalledRepository):
            GetAllPackagesInstalledInteract = GetAllPackagesInstalledInteract(packageInstalledRepository)


    /**
     * Provide Sync Package Usage Stats Interact
     */
    @Provides
    @Singleton
    fun provideSynPackageUsageStatsInteract(usageStatsService: IUsageStatsService, packageUsageStatsRepository: IPackageUsageStatsRepository):
            SynPackageUsageStatsInteract = SynPackageUsageStatsInteract(usageStatsService,
                        packageUsageStatsRepository)
}