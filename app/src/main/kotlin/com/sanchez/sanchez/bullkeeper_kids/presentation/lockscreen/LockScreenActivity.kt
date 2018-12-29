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
import kotlinx.android.synthetic.main.content_lock_screen.*
import java.util.*
import javax.inject.Inject
import android.graphics.BitmapFactory
import android.util.Base64
import android.app.Activity
import android.app.ActivityManager
import android.view.WindowManager
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Lock Screen Activity
 */
class LockScreenActivity : AppCompatActivity() {


    /**
     * App Component
     */
    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    companion object {

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
        const val UNLOCK_APP_ACTION = "com.sanchez.sergio.unlock.app"

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context, packageName: String?, appName: String?,
                          icon: String?, appRule: String?): Intent {
            val intent = Intent(context, LockScreenActivity::class.java)
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
     * App Blocked Stream Id
     */
    private var appBlockedStreamId = -1


    /**
     *
     */
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    private var mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == UNLOCK_APP_ACTION) {
                finish()
            }
        }
    }

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_screen)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        appComponent.inject(this)
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction(UNLOCK_APP_ACTION)
        mLocalBroadcastManager?.registerReceiver(mBroadcastReceiver, mIntentFilter)
        Timber.d("LOCK: On Create Called")
        showAppBlockedDetail(intent)

    }

    /**
     * On New Intent
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d("LOCK: On New Intent called")
        intent?.let { showAppBlockedDetail(it) }
    }

    /**
     * On Resume
     */
    override fun onResume() {
        super.onResume()
        // Play Emergency Sound
        appBlockedStreamId = soundManager.playSound(ISoundManager.APP_BLOCKED_SOUND, true)
    }

    /**
     * On Stop
     */
    override fun onStop() {
        super.onStop()
        soundManager.stopSound(appBlockedStreamId)
    }

    /**
     * Show App Blocked Detail
     */
    private fun showAppBlockedDetail(intent: Intent){

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
            appBlockedIcon.setImageBitmap(decodedByte)
        } else {
            appBlockedIcon.setImageResource(R.drawable.app_icon_default)
        }

        // Set Content
        contentText.text = String.format(Locale.getDefault(),
                getString(R.string.lock_screen_content_screen), appName)

        // Close App Handler
        closeApp.setOnClickListener {
            val am = getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
            am.killBackgroundProcesses(packageName)
            navigator.showHome(this)
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

}
