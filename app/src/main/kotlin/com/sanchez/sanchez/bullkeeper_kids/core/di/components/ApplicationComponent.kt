package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import android.app.Application
import android.content.Context
import com.here.oksse.OkSse
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ApplicationModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.NetModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.GlobalServiceModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.PackagesModule
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IAppsService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.SplashScreenActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.AppStatusChangedReceiver
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.PhoneCallReceiver
import com.sanchez.sanchez.bullkeeper_kids.presentation.home.HomeActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.login.SignInActivity
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import com.squareup.picasso.Picasso
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Application Component
 */
@Singleton
@Component(modules = [ ApplicationModule::class, NetModule::class,
    GlobalServiceModule::class, PackagesModule::class])
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
     * Inject into Phone Call Receiver
     */
    fun inject(phoneCallReceiver: PhoneCallReceiver)


    //Exposed to sub-graphs.
    fun navigator(): INavigator
    fun preferenceRepository(): IPreferenceRepository
    fun usageStatsService(): IUsageStatsService
    fun retrofit(): Retrofit
    fun appContext(): Context
    fun androidApplication(): AndroidApplication
    fun application(): Application
    fun picasso(): Picasso
    fun apiEndPointsHelper(): ApiEndPointsHelper
    fun oksse(): OkSse
    fun systemPackageHelper(): ISystemPackageHelper
    fun appsInstalledRepository(): IAppsInstalledRepository
    fun appsService(): IAppsService
}
