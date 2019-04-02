package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.app.Application
import android.content.Context
import android.os.Handler
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.impl.NavigatorImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IAppOverlayService
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import com.sanchez.sanchez.bullkeeper_kids.services.impl.AppOverlayServiceImpl
import com.sanchez.sanchez.bullkeeper_kids.services.impl.SystemPackageHelperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



/**
 * Application Module
 */
@Module
class ApplicationModule(private val application: AndroidApplication) {

    /**
     * Provide Application
     */
    @Provides @Singleton
    fun provideApplication(): Application = application

    /**
     * Provide Handler
     */
    @Provides @Singleton
    fun provideHandler(): Handler = Handler()

    /**
     * Provide Application
     */
    @Provides @Singleton
    fun provideAndroidApplication(): AndroidApplication = application

    /**
     * Provide Application Context
     */
    @Provides @Singleton fun provideApplicationContext(): Context = application

    /**
     * Provide System Package Helper
     */
    @Provides @Singleton fun provideSystemPackageHelper(context: Context): ISystemPackageHelper {
        return SystemPackageHelperImpl(context)
    }

    /**
     * Provide Navigator
     */
    @Provides @Singleton fun provideNavigator(preferenceRepository: IPreferenceRepository):
            INavigator = NavigatorImpl(preferenceRepository)

    /**
     * Provide App Overlay Service
     */
    @Provides @Singleton fun provideAppOverlayService(context: Context): IAppOverlayService
        = AppOverlayServiceImpl(context)



}
