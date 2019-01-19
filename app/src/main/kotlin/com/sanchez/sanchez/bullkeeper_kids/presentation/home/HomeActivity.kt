package com.sanchez.sanchez.bullkeeper_kids.presentation.home

import android.Manifest
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.services.MonitoringService
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
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

    val ENABLE_DEVICE_ADMIN_FEATURES_CODE = 666

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
     * Device Policy Manager
     */
    private lateinit var devicePolicyManager: DevicePolicyManager

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        ContextCompat.startForegroundService(this,
                Intent(this, MonitoringService::class.java))


        /**
         * val active = devicePolicyManager
        .isAdminActive(MonitoringDeviceAdminReceiver.getComponentName(this@HomeActivity))

        if (active) {
        devicePolicyManager.lockNow()
        } else {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
        MonitoringDeviceAdminReceiver.getComponentName(this@HomeActivity))
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
        "Additional text explaining why we need this permission")
        this@HomeActivity.startActivityForResult(intent, ENABLE_DEVICE_ADMIN_FEATURES_CODE)
        }
         */

    }

    /**
     * On Resume
     */
    override fun onResume() {
        super.onResume()

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

    /**
     * Get Layout Resource
     */
    override fun getLayoutRes(): Int = R.layout.activity_home

    override fun fragment(): BaseFragment = HomeActivityFragment()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == ENABLE_DEVICE_ADMIN_FEATURES_CODE
            && resultCode == Activity.RESULT_OK) {

            devicePolicyManager.lockNow()

        }
    }

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
     * Show Bed Time
     */
    override fun showBedTime() = navigator.showBedTimeScreen(this)

}
