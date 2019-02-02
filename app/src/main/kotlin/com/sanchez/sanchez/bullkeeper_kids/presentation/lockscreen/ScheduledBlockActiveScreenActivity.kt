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
import com.sanchez.sanchez.bullkeeper_kids.BuildConfig
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import kotlinx.android.synthetic.main.content_scheduled_block_active_screen.*
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Scheduled Block Active Screen
 */
class ScheduledBlockActiveScreenActivity : AppCompatActivity() {

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
        const val IDENTITY_ARG = "IDENTITY_ARG"
        const val NAME_ARG = "NAME_ARG"
        const val IMAGE_ARG = "IMAGE_ARG"
        const val START_AT_ARG = "START_AT_ARG"
        const val END_AT_ARG = "END_AT_ARG"
        const val DESCRIPTION_ARG = "DESCRIPTION_ARG"

        /**
         * Event
         */
        const val UNLOCK_APP_ACTION = "com.sanchez.sergio.unlock.app"

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context, identity: String?, name: String?, image: String?, startAt: String?,
                          endAt: String?, description: String?): Intent {
            val intent = Intent(context, ScheduledBlockActiveScreenActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(IDENTITY_ARG, identity)
            intent.putExtra(NAME_ARG, name)
            intent.putExtra(IMAGE_ARG, image)
            intent.putExtra(START_AT_ARG, startAt)
            intent.putExtra(END_AT_ARG, endAt)
            intent.putExtra(DESCRIPTION_ARG, description)
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
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

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
        setContentView(R.layout.activity_scheduled_block_active_screen)
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
        if(appBlockedStreamId != -1)
            soundManager.stopSound(appBlockedStreamId)
        // Play Emergency Sound
        appBlockedStreamId = soundManager.playSound(ISoundManager.BLOCKED_SOUND, true)
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
        val identity = intent.getStringExtra(IDENTITY_ARG)
        val name = intent.getStringExtra(NAME_ARG)
        val image = intent.getStringExtra(IMAGE_ARG)
        val startAt = intent.getStringExtra(START_AT_ARG)
        val endAt = intent.getStringExtra(END_AT_ARG)
        val description = intent.getStringExtra(DESCRIPTION_ARG)

        if (!image.isNullOrEmpty()) {

            val kid = preferenceRepository.getPrefKidIdentity()

            picasso.load(String.format("%s/children/%s/scheduled-blocks/%s/image/%s",
                    BuildConfig.BASE_URL, kid, identity, image))
                    .error(R.drawable.scheduled_block_default_icon)
                    .placeholder(R.drawable.scheduled_block_default_icon)
                    .into(scheduledBlockImage)

        }


        scheduledBlockName.text = name
        scheduledBlockDescription.text = description
        scheduledBlockTime.text = String.format(getString(R.string.scheduled_block_time_format),
                startAt, endAt)

        // Close App Handler
        closeApp.setOnClickListener {
            val am = getSystemService(Activity.ACTIVITY_SERVICE)
                    as ActivityManager
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
