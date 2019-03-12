package com.sanchez.sanchez.bullkeeper_kids.presentation.home

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.AwakenMonitoringServiceBroadcastReceiver
import com.sanchez.sanchez.bullkeeper_kids.presentation.services.MonitoringService
import com.sanchez.sanchez.bullkeeper_kids.presentation.services.MonitoringService.Companion.SETTINGS_STATUS_CHANGED_ACTION
import com.sanchez.sanchez.bullkeeper_kids.services.IGeolocationService
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import com.sanchez.sanchez.bullkeeper_kids.services.impl.GeolocationServiceImpl
import kotlinx.android.synthetic.main.app_translucent_toolbar.*
import javax.inject.Inject

/**
 * Home Activity
 */
class HomeActivity : BaseActivity(),
        HasComponent<ApplicationComponent>, IHomeActivityHandler {


    val TAG = "HOME_ACTIVITY"

    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                    or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            return intent
        }
    }


    /**
     * App Component
     */
    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    /**
     * App Component
     */
    override val component: ApplicationComponent
        get() = appComponent


    /**
     * Dependencies
     * =================
     */

    /**
     * Usage Stats Service
     */
    @Inject
    internal lateinit var usageStatsService: IUsageStatsService

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: INavigator

    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * Geolocation Service
     */
    @Inject
    internal lateinit var geolocationService: IGeolocationService

    /**
     * Fun Time Changed Event Handler
     */
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    private var mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(intent.action == SETTINGS_STATUS_CHANGED_ACTION) {
                checkSettingsStatus()
            }
        }
    }

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction(SETTINGS_STATUS_CHANGED_ACTION)
        mLocalBroadcastManager?.registerReceiver(mBroadcastReceiver, mIntentFilter)

        ContextCompat.startForegroundService(this,
                Intent(this, MonitoringService::class.java))

    }

    /**
     * On Resume
     */
    override fun onResume() {
        super.onResume()

        checkSettingsStatus()
        // Save the status of permissions

        preferenceRepository.setAccessFineLocationEnabled(
                !permissionManager.shouldAskPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        )

        preferenceRepository.setReadContactsEnabled(
                !permissionManager.shouldAskPermission(Manifest.permission.READ_CONTACTS)
        )

        preferenceRepository.setReadCallLogEnabled(
                !permissionManager.shouldAskPermission(Manifest.permission.READ_CALL_LOG)
        )

        preferenceRepository.setReadSmsEnabled(
                !permissionManager.shouldAskPermission(Manifest.permission.READ_SMS)
        )

        preferenceRepository.setWriteExternalStorageEnabled(
                !permissionManager.shouldAskPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        )

        preferenceRepository.setUsageStatsAllowed(
                usageStatsService.isUsageStatsAllowed()
        )

        preferenceRepository.setAdminAccessEnabled(
                isDevicePolicyManagerActive()
        )

        preferenceRepository.setHighAccuraccyLocationEnabled(
                geolocationService.isHighAccuraccyLocationEnabled()
        )


        AwakenMonitoringServiceBroadcastReceiver.scheduledAt(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName"))
            startActivityForResult(intent, 1)
        }

    }

    /**
     * Get Layout Resource
     */
    override fun getLayoutRes(): Int = R.layout.activity_home

    /**
     * Fragment
     */
    override fun fragment(): BaseFragment = HomeActivityFragment()


    /**
     * Show Sos Screen
     */
    override fun showSosScreen() = navigator.showSosScreen(this)

    /**
     * Show Pick Me Up Screen
     */
    override fun showPickMeUpScreen() = navigator.showPickMeUpScreen(this)

    /**
     * Show Time Bank Screen
     */
    override fun showTimeBankScreen() = navigator.showTimeBankScreen(this)

    /**
     * Show Chat Action
     */
    override fun showChatAction() = navigator.showConversationList(this)

    /**
     * Check Settings Status
     */
    private fun checkSettingsStatus() {
        if(preferenceRepository.isSettingsEnabled()) {
            appSettings.visibility = View.VISIBLE
            appSettings.setOnClickListener {
                navigator.showSettingsScreen(this)
            }

        } else {
            appSettings.visibility = View.GONE
            appSettings.setOnClickListener(null)
        }
    }

}
