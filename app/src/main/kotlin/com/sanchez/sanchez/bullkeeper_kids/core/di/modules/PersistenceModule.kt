package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageUsageStatsRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.PackageInstalledRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.PackageUsageStatsRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Persistence Module
 */
@Module
class PersistenceModule(private val application: AndroidApplication)  {


    /**
     * Provide Package Installed Repository
     */
    @Provides
    @Singleton
    fun providePackageInstalledRepository(): IPackageInstalledRepository =
            PackageInstalledRepositoryImpl()

    /**
     * Provide Package Usage Stats Repository
     */
    @Provides
    @Singleton
    fun providePackageUsageStatsRepository(): IPackageUsageStatsRepository =
            PackageUsageStatsRepositoryImpl()

}