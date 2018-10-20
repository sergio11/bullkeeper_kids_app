package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.GetBlockedPackagesInteract
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import com.sanchez.sanchez.bullkeeper_kids.services.impl.SystemPackageHelperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Application Module
 */
@Module
class ApplicationModule(private val application: AndroidApplication) {

    @Provides @Singleton fun provideApplicationContext(): Context = application

    /**
     * Provide System Package Helper
     */
    @Provides @Singleton fun provideSystemPackageHelper(): ISystemPackageHelper {
        return SystemPackageHelperImpl()
    }



}
