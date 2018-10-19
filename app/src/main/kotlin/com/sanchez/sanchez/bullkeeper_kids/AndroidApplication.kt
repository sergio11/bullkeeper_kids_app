package com.sanchez.sanchez.bullkeeper_kids

import android.app.Application
import com.facebook.stetho.Stetho
import com.sanchez.sanchez.bullkeeper_kids.core.di.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.ApplicationModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.DaggerApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.PersistenceModule
import com.squareup.leakcanary.LeakCanary
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import java.util.regex.Pattern

/**
 * Android Application
 */
class AndroidApplication : Application() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .persistenceModule(PersistenceModule(this))
                .build()
    }

    /**
     * On Create
     */
    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        this.initializeLeakDetection()
        this.initializeRealm()
        this.initializeStetho()
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

}
