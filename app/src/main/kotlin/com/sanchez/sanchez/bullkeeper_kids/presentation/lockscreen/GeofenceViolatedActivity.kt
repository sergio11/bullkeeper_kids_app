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
import javax.inject.Inject
import android.app.Activity
import android.app.ActivityManager
import android.view.WindowManager
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import kotlinx.android.synthetic.main.content_geofence_violated.*
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Geofence Violated Screen
 */
class GeofenceViolatedActivity : AppCompatActivity() {

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
        const val NAME_ARG = "NAME_ARG"
        const val TYPE_ARG = "TYPE_ARG"
        const val RADIUS_ARG = "RADIUS_ARG"

        /**
         * Event
         */
        const val UNLOCK_GEOFENCE_LOCK = "com.sanchez.sergio.unlock.geofence"

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context, name: String?, type: String?, radius: Float?): Intent {
            val intent = Intent(context, GeofenceViolatedActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(NAME_ARG, name)
            intent.putExtra(TYPE_ARG, type)
            intent.putExtra(RADIUS_ARG, radius)
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
     * Geofence Stream Id
     */
    private var geofenceStreamId = -1


    /**
     *
     */
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    private var mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == UNLOCK_GEOFENCE_LOCK) {
                finish()
            }
        }
    }

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofence_violated)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        appComponent.inject(this)
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction(UNLOCK_GEOFENCE_LOCK)
        mLocalBroadcastManager?.registerReceiver(mBroadcastReceiver, mIntentFilter)
        showGeofenceDetail(intent)

    }

    /**
     * On New Intent
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d("LOCK: On New Intent called")
        intent?.let { showGeofenceDetail(it) }
    }

    /**
     * On Resume
     */
    override fun onResume() {
        super.onResume()
        if(geofenceStreamId != -1)
            soundManager.stopSound(geofenceStreamId)
        // Blocked Sound
        geofenceStreamId = soundManager.playSound(
                ISoundManager.BLOCKED_SOUND, true)
    }

    /**
     * On Stop
     */
    override fun onStop() {
        super.onStop()
        soundManager.stopSound(geofenceStreamId)
    }

    /**
     * Show Geofence Detail
     */
    private fun showGeofenceDetail(intent: Intent){

        // Get Args
        val name = intent.getStringExtra(NAME_ARG)
        val type = intent.getStringExtra(TYPE_ARG)
        val radius = intent.getFloatExtra(RADIUS_ARG, 0.0f)


        geofenceName.text = if(!name.isNullOrEmpty()) name else
            getString(R.string.geofence_violated_title_default)
        geofenceDescription.text = when(type) {
            "TRANSITION_ENTER" -> getString(R.string.geofence_transition_enter_description)
            "TRANSITION_EXIT" -> getString(R.string.geofence_transition_exit_description)
            else -> getString(R.string.geofence_transition_exit_description)
        }

        close.setOnClickListener {
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
