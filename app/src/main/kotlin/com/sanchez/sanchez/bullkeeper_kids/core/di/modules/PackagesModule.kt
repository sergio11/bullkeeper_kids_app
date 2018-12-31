package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IAppsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageUsageStatsRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.AppsInstalledRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.PackageUsageStatsRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.GetAllAppsInstalledInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.SyncPackageUsageStatsInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.SynchronizeInstalledPackagesInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Packages Module
 */
@Module
class PackagesModule {

    /**
     * Provide Package Installed Repository
     */
    @Provides
    @Singleton
    fun providePackageInstalledRepository(): IAppsInstalledRepository =
            AppsInstalledRepositoryImpl()

    /*
     * Provide Package Usage Stats Repository
     */
    @Provides
    @Singleton
    fun providePackageUsageStatsRepository(): IPackageUsageStatsRepository =
            PackageUsageStatsRepositoryImpl()

    /**
     * Provide Apps Service
     */
    @Provides
    @Singleton
    fun provideAppsService(retrofit: Retrofit) : IAppsService
        = retrofit.create(IAppsService::class.java)

    /**
     * Provide Get All Packages Installed Interact
     */
    @Provides
    @Singleton
    fun provideGetAllPackagesInstalledInteract(retrofit: Retrofit, appsInstalledRepository: IAppsInstalledRepository):
            GetAllAppsInstalledInteract = GetAllAppsInstalledInteract(retrofit, appsInstalledRepository)


    /**
     * Provide Sync Package Usage Stats Interact
     */
    @Provides
    @Singleton
    fun provideSynPackageUsageStatsInteract(
            retrofit: Retrofit,
            usageStatsService: IUsageStatsService,
            packageUsageStatsRepository: IPackageUsageStatsRepository,
            appsInstalledRepository: IAppsInstalledRepository,
            appsService: IAppsService,
            preferenceRepository: IPreferenceRepository):
            SyncPackageUsageStatsInteract = SyncPackageUsageStatsInteract(retrofit, usageStatsService,
                        packageUsageStatsRepository, appsInstalledRepository, appsService, preferenceRepository)


    /**
     * Provide Synchronize Installed Packages Interact
     */
    @Provides
    @Singleton
    fun provideSynchronizeInstalledPackagesInteract(
            retrofit: Retrofit,
            systemPackageHelper: ISystemPackageHelper,
            appsInstalledRepository: IAppsInstalledRepository,
            appsService: IAppsService,
            preferenceRepository: IPreferenceRepository
    ): SynchronizeInstalledPackagesInteract
        = SynchronizeInstalledPackagesInteract(retrofit, systemPackageHelper,
            appsInstalledRepository, appsService, preferenceRepository)
}