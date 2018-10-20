package com.sanchez.sanchez.bullkeeper_kids

import android.app.Application
import android.content.Intent
import android.support.v4.content.ContextCompat
import com.facebook.stetho.Stetho
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.DaggerApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.DaggerServiceComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ServiceComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ApplicationModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.PackagesModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.PersistenceModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ServicesModule
import com.sanchez.sanchez.bullkeeper_kids.presentation.services.MonitoringService
import com.squareup.leakcanary.LeakCanary
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import java.util.regex.Pattern

/**
 * Android Application
 */
class AndroidApplication : Application() {

    /**
     * Application Component
     */
    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .persistenceModule(PersistenceModule(this))
                .servicesModule(ServicesModule(this))
                .build()
    }

    /**
     * Service Component
     */
    val serviceComponent: ServiceComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerServiceComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .servicesModule(ServicesModule(this))
                .packagesModule(PackagesModule())
                .persistenceModule(PersistenceModule(this))
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

        INSTANCE = this

        this.injectMembers()
        this.initializeLeakDetection()
        this.initializeRealm()
        this.initializeStetho()
        this.initializeServices()


    }

    /**
     * Inject Members
     */
    private fun injectMembers() = appComponent.inject(this)

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
        ContextCompat.startForegroundService(this,
                Intent(this, MonitoringService::class.java))
    }

}
