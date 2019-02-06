package com.sanchez.sanchez.bullkeeper_kids.presentation.lockscreen

import android.app.Activity
import android.app.ActivityManager
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
import javax.inject.Inject
import android.view.WindowManager
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import kotlinx.android.synthetic.main.content_settings_lock_screen.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Settings Lock Screen Activity
 */
class SettingsLockScreenActivity : AppCompatActivity() {

    /**
     * App Component
     */
    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    companion object {

        /**
         * Event
         */
        const val UNLOCK_SETTINGS_SCREEN = "com.sanchez.sergio.settings.unlock"

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context): Intent {
            val intent = Intent(context, SettingsLockScreenActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
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
     * Stream Id
     */
    private var streamId = -1


    /**
     *
     */
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    private var mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == UNLOCK_SETTINGS_SCREEN) {
                finish()
            }
        }
    }

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_lock_screen)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        appComponent.inject(this)
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction(UNLOCK_SETTINGS_SCREEN)
        mLocalBroadcastManager?.registerReceiver(mBroadcastReceiver, mIntentFilter)
        // Close App Handler
        close.setOnClickListener {
            val am = getSystemService(Activity.ACTIVITY_SERVICE)
                    as ActivityManager
            am.killBackgroundProcesses(packageName)
            navigator.showHome(this)
        }
    }


    /**
     * On Resume
     */
    override fun onResume() {
        super.onResume()
        if(streamId != -1)
            soundManager.stopSound(streamId)
        // Blocked Sound
        streamId = soundManager.playSound(
                ISoundManager.BLOCKED_SOUND, true)
    }

    /**
     * On Stop
     */
    override fun onStop() {
        super.onStop()
        soundManager.stopSound(streamId)
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
