package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import android.app.Application
import android.content.Context
import com.here.oksse.OkSse
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.*
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IAppsService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls.AddCallDetailsFromTerminalInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls.SynchronizeTerminalCallHistoryInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.contacts.SynchronizeTerminalContactsInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.location.GetAddressFromCurrentLocationInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.sms.SynchronizeTerminalSMSInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.SplashScreenActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.bedtime.BedTimeActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.bedtime.BedTimeActivityFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.AppStatusChangedReceiver
import com.sanchez.sanchez.bullkeeper_kids.presentation.home.HomeActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.lockscreen.LockScreenActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.login.SignInActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.pickmeup.PickMeUpActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.pickmeup.PickMeUpActivityFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.timebank.TimeBankActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.timebank.TimeBankActivityFragment
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
@Component(modules = [
    ApplicationModule::class, NetModule::class,
    GlobalServiceModule::class, PackagesModule::class,
    CallDetailsModule::class, SmsModule::class,
    ContactsModule::class])
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
     * Inject into Lock Sceen
     */
    fun inject(lockScreenActivity: LockScreenActivity)


    /**
     * Inject into Pick Me Up Activity
     */
    fun inject(pickMeUpActivity: PickMeUpActivity)

    /**
     * Inject into Pick Me Up Fragment
     */
    fun inject(pickMeUpActivityFragment: PickMeUpActivityFragment)

    /**
     * Inject into Time Bank Activity
     */
    fun inject(timeBankActivity: TimeBankActivity)

    /**
     * Inject into Time Bank Up Activity Fragment
     */
    fun inject(timeBankActivityFragment: TimeBankActivityFragment)

    /**
     * Inject into Bed Time Activity
     */
    fun inject(bedTimeActivity: BedTimeActivity)

    /**
     * Inject into Bed Time Activity Fragment
     */
    fun inject(bedTimeActivityFragment: BedTimeActivityFragment)


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
    fun synchronizeTerminalCallHistoryInteract(): SynchronizeTerminalCallHistoryInteract
    fun addCallDetailsFromTerminalInteract(): AddCallDetailsFromTerminalInteract
    fun synchronizeTerminalSMSInteract(): SynchronizeTerminalSMSInteract
    fun synchronizeTerminalContactsInteract(): SynchronizeTerminalContactsInteract
    fun getAddressFromCurrentLocationInteract(): GetAddressFromCurrentLocationInteract
    fun soundManager(): ISoundManager
}
