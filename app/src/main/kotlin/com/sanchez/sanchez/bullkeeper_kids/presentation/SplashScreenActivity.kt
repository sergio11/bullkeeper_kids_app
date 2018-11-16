package com.sanchez.sanchez.bullkeeper_kids.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import javax.inject.Inject

/**
 * Splash Screen Activity
 */
class SplashScreenActivity : AppCompatActivity() {

    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    /**
     * Navigator
     */
    @Inject internal lateinit var navigator: INavigator

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        navigator.showAppTutorial(this)
    }
}
