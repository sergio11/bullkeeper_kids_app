package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ApplicationModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.NetModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.PersistenceModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.GlobalServiceModule
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.SplashScreenActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.AppStatusChangedReceiver
import com.sanchez.sanchez.bullkeeper_kids.presentation.home.HomeActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.login.SignInActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.LinkDeviceTutorialActivity
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import com.squareup.picasso.Picasso
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Application Component
 */
@Singleton
@Component(modules = [ ApplicationModule::class,
    PersistenceModule::class, NetModule::class,
    GlobalServiceModule::class])
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

    /**
     * Inject Into Sign In Activity
     */
    fun inject(signInActivity: SignInActivity)

    /**
     * Inject into App Tutorial Activity
     */
    fun inject(appTutorialActivity: LinkDeviceTutorialActivity)

    //Exposed to sub-graphs.
    fun navigator(): INavigator
    fun preferenceRepository(): IPreferenceRepository
    fun usageStatsService(): IUsageStatsService
    fun retrofit(): Retrofit
    fun appContext(): Context
    fun picasso(): Picasso
    fun apiEndPointsHelper(): ApiEndPointsHelper

}
