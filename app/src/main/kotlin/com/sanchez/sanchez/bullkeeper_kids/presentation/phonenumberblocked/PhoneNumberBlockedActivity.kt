package com.sanchez.sanchez.bullkeeper_kids.presentation.phonenumberblocked

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.WindowManager
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import javax.inject.Inject
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import kotlinx.android.synthetic.main.content_phone_number_blocked.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

/**
 * Phone Number Blocked Activity
 */
class PhoneNumberBlockedActivity : AppCompatActivity() {


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
        const val PHONE_NUMBER_BLOCKED = "PHONE_NUMBER_BLOCKED"
        const val BLOCKED_AT = "BLOCKED_AT"
        const val UNKNOWN_EMITTER = "UNKNOWN_EMITTER"

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context, phoneNumberBlocked: String, blockedAt: String): Intent {
            val intent = Intent(context, PhoneNumberBlockedActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(PHONE_NUMBER_BLOCKED, phoneNumberBlocked)
            intent.putExtra(BLOCKED_AT, blockedAt)
            return intent
        }

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context): Intent {
            val intent = Intent(context, PhoneNumberBlockedActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(UNKNOWN_EMITTER, true)
            return intent
        }

    }

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
     * Blocked Stream Id
     */
    private var blockedStreamId: Int = -1


    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number_blocked)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        appComponent.inject(this)
        showPhoneNumberBlockedDetail(intent)
    }

    /**
     * On New Intent
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { showPhoneNumberBlockedDetail(it) }

    }

    /**
     * Show Phone Number Blocked Detail
     */
    private fun showPhoneNumberBlockedDetail(intent: Intent){

        if(intent.hasExtra(UNKNOWN_EMITTER)) {
            contentText.text = getString(R.string.phone_number_blocked_unknown_emitter)
            phoneNumberBlockedAt.visibility = GONE

        } else {

            // Get Args
            val phoneNumberBlocked = intent.getStringExtra(PHONE_NUMBER_BLOCKED)
            val blockedAt = intent.getStringExtra(BLOCKED_AT)

            // Phone Number Blocked
            contentText.text = String.format(Locale.getDefault(),
                    getString(R.string.phone_number_blocked_content), phoneNumberBlocked)

            // Blocked At
            phoneNumberBlockedAt.text = String.format(
                    Locale.getDefault(), getString(R.string.phone_number_blocked_blocked_at),
                    blockedAt
            )

        }

        close.setOnClickListener {
            finishAndRemoveTask()
        }

    }

    /**
     * Attach Base Context
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    /**
     * On Resume
     */
    override fun onResume() {
        super.onResume()
        if(blockedStreamId != -1)
            soundManager.stopSound(blockedStreamId)
        // Play Phone Number Blocked Sound
        blockedStreamId = soundManager.playSound(ISoundManager.PHONE_NUMBER_BLOCKED_SOUND, true)
    }

    /**
     * On Stop
     */
    override fun onStop() {
        super.onStop()
        soundManager.stopSound(blockedStreamId)
    }
}
