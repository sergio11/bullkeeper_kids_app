package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.PackageInstalledRepositoryImpl
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

}