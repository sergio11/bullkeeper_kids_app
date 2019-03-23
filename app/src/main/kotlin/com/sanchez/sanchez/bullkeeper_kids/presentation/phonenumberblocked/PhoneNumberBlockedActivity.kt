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

    /**
     * Phone Blocks Type Enum
     */
    enum class PhoneBlocksTypeEnum {
        NUMBER_BLOCKED,
        UNKNOWN_EMITTER,
        CALLS_NOT_ALLOWED
    }

    companion object {

        /**
         * Args
         */
        const val BLOCK_TYPE_ARG = "BLOCK_TYPE_ARG_ARG"
        const val PHONE_NUMBER_BLOCKED_ARG = "PHONE_NUMBER_BLOCKED_ARG"
        const val BLOCKED_AT_ARG = "BLOCKED_AT_ARG"

        /**
         * Calling Intent
         * @param context
         * @param phoneNumberBlocked
         * @param blockedAt
         */
        fun callingIntent(context: Context, phoneNumberBlocked: String, blockedAt: String): Intent {
            val intent = Intent(context, PhoneNumberBlockedActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(PHONE_NUMBER_BLOCKED_ARG, phoneNumberBlocked)
            intent.putExtra(BLOCKED_AT_ARG, blockedAt)
            intent.putExtra(BLOCK_TYPE_ARG, PhoneBlocksTypeEnum.NUMBER_BLOCKED)
            return intent
        }

        /**
         * Calling Intent
         * @param context
         * @param callsEnabled
         */
        fun callingIntent(context: Context, callsEnabled: Boolean = false): Intent {
            val intent = Intent(context, PhoneNumberBlockedActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(BLOCK_TYPE_ARG, if(callsEnabled)
                    PhoneBlocksTypeEnum.UNKNOWN_EMITTER else PhoneBlocksTypeEnum.CALLS_NOT_ALLOWED)
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

        if(intent.hasExtra(BLOCK_TYPE_ARG)) {

            val blockType = intent.extras?.get(BLOCK_TYPE_ARG) as PhoneBlocksTypeEnum

            when(blockType) {

                PhoneBlocksTypeEnum.NUMBER_BLOCKED -> {
                    // Get Args
                    val phoneNumberBlocked = intent.getStringExtra(PHONE_NUMBER_BLOCKED_ARG)
                    val blockedAt = intent.getStringExtra(BLOCK_TYPE_ARG)

                    // Phone Number Blocked
                    contentText.text = String.format(Locale.getDefault(),
                            getString(R.string.phone_number_blocked_content), phoneNumberBlocked)

                    // Blocked At
                    phoneNumberBlockedAt.text = String.format(
                            Locale.getDefault(), getString(R.string.phone_number_blocked_blocked_at),
                            blockedAt
                    )
                }

                PhoneBlocksTypeEnum.CALLS_NOT_ALLOWED -> {
                    contentText.text = getString(R.string.phone_calls_not_allowed)
                    phoneNumberBlockedAt.visibility = GONE
                }

                PhoneBlocksTypeEnum.UNKNOWN_EMITTER -> {
                    contentText.text = getString(R.string.phone_number_blocked_unknown_emitter)
                    phoneNumberBlockedAt.visibility = GONE
                }

            }

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
