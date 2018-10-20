package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.GetAllPackagesInstalledInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.GetBlockedPackagesInteract
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ PersistenceModule::class ])
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

}