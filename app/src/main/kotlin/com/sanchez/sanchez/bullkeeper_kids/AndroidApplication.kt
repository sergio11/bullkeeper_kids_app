package com.sanchez.sanchez.bullkeeper_kids

import android.app.Application
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.util.Log
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.facebook.stetho.Stetho
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.*
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.*
import com.sanchez.sanchez.bullkeeper_kids.presentation.services.MonitoringService
import com.squareup.leakcanary.LeakCanary
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import timber.log.Timber
import java.util.regex.Pattern
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


/**
 * Android Application
 */
class AndroidApplication : Application(){

    /**
     * Application Component
     */
    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .globalServiceModule(GlobalServiceModule(this))
                .applicationModule(ApplicationModule(this))
                .netModule(NetModule(this))
                .build()
    }

    /**
     * Service Component
     */
    val serviceComponent: ServiceComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerServiceComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .globalServiceModule(GlobalServiceModule(this))
                .packagesModule(PackagesModule())
                .netModule(NetModule(this))
                .build()
    }

    /**
     * Service Component
     */
    val callReceiverComponent: CallReceiverComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerCallReceiverComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .globalServiceModule(GlobalServiceModule(this))
                .callDetailsModule(CallDetailsModule())
                .netModule(NetModule(this))
                .build()
    }

    /**
     * SMS Receiver Component
     */
    val smsReceiverComponent: SMSReceiverComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerSMSReceiverComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .globalServiceModule(GlobalServiceModule(this))
                .smsModule(SmsModule())
                .netModule(NetModule(this))
                .build()
    }


    companion object {

        @JvmStatic
        lateinit var INSTANCE: AndroidApplication

    }


    /**
     * On Create
     */
    override fun onCreate() {
        super.onCreate()

        // Apply Common Config
        onCommonConfig()

        if (BuildConfig.DEBUG) {
            onDebugConfig()
        } else {
            onReleaseConfig()
        }

        INSTANCE = this

    }

    /**
     * On Common Config
     */
    private fun onCommonConfig(){

        this.injectMembers()
        this.initializeCalligraphy()
        this.initializeRealm()
        this.initializeServices()

        // Chrash Screen
        CaocConfig.Builder.create().apply()
    }

    /**
     * On Debug Config
     */
    private fun onDebugConfig() {
        // Debug Tree
        Timber.plant(Timber.DebugTree())

        this.initializeLeakDetection()

        this.initializeStetho()
    }

    /**
     * On Release Config
     */
    private fun onReleaseConfig(){
        // Reporting Tree
        Timber.plant(CrashReportingTree())
    }

    /**
     * Inject Members
     */
    private fun injectMembers() = appComponent.inject(this)

    /**
     * Initialize Calligraphy
     */
    private fun initializeCalligraphy() {

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/VAGRoundedBT.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }


    /**
     * Initializae Leak Detection
     */
    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }

    /**
     * Initialize Stetho
     */
    private fun initializeStetho() {
        if(BuildConfig.DEBUG) Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                RealmInspectorModulesProvider.builder(this)
                                        .withFolder(getCacheDir())
                                        .withMetaTables()
                                        .withDescendingOrder()
                                        .withLimit(1000)
                                        .databaseNamePattern(Pattern.compile(".+\\.realm"))
                                        .build()

                        )
                        .build())
    }

    /**
     * Initialize Realm
     */
    private fun initializeRealm() = Realm.init(this)

    /**
     * Initialize Services
     */
    private fun initializeServices() {

    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {


        override fun log(priority: Int, tag: String, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

            //FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    //FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    //FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }

}
