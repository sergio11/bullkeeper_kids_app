package com.sanchez.sanchez.bullkeeper_kids.presentation.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.DaggerSettingsComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.SettingsComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import kotlinx.android.synthetic.main.sos_toolbar.*
import javax.inject.Inject

/**
 * Settings Activity
 */
class SettingsActivity : BaseActivity(),
        HasComponent<SettingsComponent>, ISettingsActivityHandler {

    /**
     * Settings Component
     */
    private val settingsComponent: SettingsComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerSettingsComponent
                .builder()
                .applicationComponent((application as AndroidApplication).appComponent)
                .activityModule(ActivityModule(this))
                .build()
    }


    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator : INavigator

    /**
     * Component
     */
    override val component: SettingsComponent
        get() = settingsComponent

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsComponent.inject(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        backIcon.setOnClickListener{
            navigator.showHome(this)
        }
    }

    /**
     * Get Layout Resource
     */
    override fun getLayoutRes(): Int = R.layout.activity_settings

    /**
     * Fragment
     */
    override fun fragment(): BaseFragment = SettingsActivityFragment()

    /**
     * Show Device Admin Settings
     */
    override fun showDeviceAdminSettings() {
        navigator.showEnableAdminDeviceFeatures(this)
    }

    /**
     * Show Usage Access Settings
     */
    override fun showUsageAccessSettings() {
        navigator.showUsageAccessSettings(this)
    }

    /**
     * Show Location Source Settings
     */
    override fun showLocationSourceSettings() {
        navigator.showLocationSourceSettings(this)
    }

}
