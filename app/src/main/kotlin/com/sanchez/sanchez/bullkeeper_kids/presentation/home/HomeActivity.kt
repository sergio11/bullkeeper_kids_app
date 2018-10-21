package com.sanchez.sanchez.bullkeeper_kids.presentation.home

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.Navigator
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.MonitoringDeviceAdminReceiver
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

/**
 * Home Activity
 */
class HomeActivity : AppCompatActivity() {

    val TAG = "HOME_ACTIVITY"

    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    val ENABLE_DEVICE_ADMIN_FEATURES_CODE = 666

    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }


    @Inject
    internal lateinit var usageStatsService: IUsageStatsService

    /**
     * Navigator
     */
    @Inject internal lateinit var navigator: Navigator

    private lateinit var devicePolicyManager: DevicePolicyManager

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        appComponent.inject(this)

        if (!usageStatsService.isUsageStatsAllowed())
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))

        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        fab.setOnClickListener { view ->
            run {

                val active = devicePolicyManager
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

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == ENABLE_DEVICE_ADMIN_FEATURES_CODE
            && resultCode == Activity.RESULT_OK) {

            devicePolicyManager.lockNow()

        }


    }
}
