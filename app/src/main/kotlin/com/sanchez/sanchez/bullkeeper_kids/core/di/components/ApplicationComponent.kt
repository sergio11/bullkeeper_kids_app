package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ApplicationModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.NetModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.PersistenceModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ServicesModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.viewmodel.ViewModelModule
import com.sanchez.sanchez.bullkeeper_kids.presentation.SplashScreenActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.AppStatusChangedReceiver
import com.sanchez.sanchez.bullkeeper_kids.presentation.home.HomeActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Application Component
 */
@Singleton
@Component(modules = [ ApplicationModule::class,
    ViewModelModule::class, PersistenceModule::class, NetModule::class,
    ServicesModule::class])
interface ApplicationComponent {

    /**
     * Inject into Android Application
     */
    fun inject(application: AndroidApplication)

    /**
     * Inject into Splash Screen Activity
     */
    fun inject(splashScreenActivity: SplashScreenActivity)

    /**
     * Inject into Home Activity
     */
    fun inject(homeActivity: HomeActivity)

    /**
     * Inject into App Status Changed Receiver
     */
    fun inject(appStatusChangeReceiver: AppStatusChangedReceiver)
}
