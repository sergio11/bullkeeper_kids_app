package com.sanchez.sanchez.bullkeeper_kids.presentation.lockscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sanchez.sanchez.bullkeeper_kids.R
import android.content.BroadcastReceiver
import android.support.v4.content.LocalBroadcastManager
import android.content.IntentFilter
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.squareup.picasso.Picasso
import java.util.*
import javax.inject.Inject
import android.graphics.BitmapFactory
import android.util.Base64
import android.app.Activity
import android.app.ActivityManager
import android.net.Uri
import android.view.WindowManager
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import kotlinx.android.synthetic.main.content_disabled_app_screen.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Disabled App Screen Activity
 */
class DisabledAppScreenActivity : AppCompatActivity() {

    /**
     * App Component
     */
    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    companion object {

        /**
         * Request Uninstall Package
         */
        const val  REQUEST_UNINSTALL_PACKAGE = 1122

        /**
         * Args
         */
        const val PACKAGE_NAME_ARG = "PACKAGE_NAME_ARG"
        const val APP_NAME_ARG = "APP_NAME_ARG"
        const val APP_ICON_ARG = "ICON_ARG"
        const val APP_RULE_ARG = "APP_RULE_ARG"

        /**
         * Event
         */
        const val ENABLE_APP_ACTION = "com.sanchez.sergio.enable.app"

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context, packageName: String?, appName: String?,
                          icon: String?, appRule: String?): Intent {
            val intent = Intent(context, DisabledAppScreenActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(PACKAGE_NAME_ARG, packageName)
            intent.putExtra(APP_NAME_ARG, appName)
            intent.putExtra(APP_ICON_ARG, icon)
            intent.putExtra(APP_RULE_ARG, appRule)
            return intent
        }
    }

    /**
     * Picasso
     */
    @Inject
    internal lateinit var picasso: Picasso


    /**
     * Sound Manager
     */
    @Inject
    internal lateinit var soundManager: ISoundManager

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: INavigator

    /**
     * App Disabled Stream Id
     */
    private var appDisabledStreamId = -1


    /**
     *
     */
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    private var mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ENABLE_APP_ACTION) {
                finish()
            }
        }
    }

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disabled_app_screen)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        appComponent.inject(this)
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction(ENABLE_APP_ACTION)
        mLocalBroadcastManager?.registerReceiver(mBroadcastReceiver, mIntentFilter)
        showDisabledAppDetail(intent)

    }

    /**
     * On New Intent
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { showDisabledAppDetail(it) }
    }

    /**
     * On Resume
     */
    override fun onResume() {
        super.onResume()
        if(appDisabledStreamId != -1)
            soundManager.stopSound(appDisabledStreamId)
        appDisabledStreamId = soundManager.playSound(ISoundManager.BLOCKED_SOUND, true)
    }

    /**
     * On Stop
     */
    override fun onStop() {
        super.onStop()
        soundManager.stopSound(appDisabledStreamId)
    }

    /**
     * Show Disabled App Detail
     */
    private fun showDisabledAppDetail(intent: Intent){

        // Get Args
        val packageName = intent.getStringExtra(PACKAGE_NAME_ARG)
        val appName = intent.getStringExtra(APP_NAME_ARG)
        val appIcon = intent.getStringExtra(APP_ICON_ARG)
        val appRule = intent.getStringExtra(APP_RULE_ARG)


        // Set App Icon
        if(!appIcon.isNullOrEmpty()) {
            val decodedString = Base64.decode(appIcon,
                    Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            appDisabledIcon.setImageBitmap(decodedByte)
        } else {
            appDisabledIcon.setImageResource(R.drawable.app_icon_default)
        }

        // Set Content
        contentText.text = String.format(Locale.getDefault(),
                getString(R.string.app_disabled_screen_content_screen), appName)

        // Close App Handler
        unInstallApp.setOnClickListener {
            val am = getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
            am.killBackgroundProcesses(packageName)
            startActivityForResult(Intent(Intent.ACTION_UNINSTALL_PACKAGE).apply {
                data = Uri.parse("package:$packageName")
            }, REQUEST_UNINSTALL_PACKAGE)
        }

    }

    /**
     * Attach Base Context
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    /**
     * On Destroy
     */
    override fun onDestroy() {
        super.onDestroy()
        mLocalBroadcastManager?.unregisterReceiver(mBroadcastReceiver)
    }

    /**
     * On Activity Result
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_UNINSTALL_PACKAGE && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

}
