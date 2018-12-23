package com.sanchez.sanchez.bullkeeper_kids.presentation.crash

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.sanchez.sanchez.bullkeeper_kids.R
import kotlinx.android.synthetic.main.activity_crash_screen.*

/**
 * Crash Error Activity
 */
class CrashErrorActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_screen)
        window.setBackgroundDrawableResource(R.drawable.background_13)


        val config = CustomActivityOnCrash.getConfigFromIntent(intent)

        if (config == null) {
            //This should never happen - Just finish the activity to avoid a recursive crash.
            finish()
            return
        }

        if (config.isShowRestartButton && config.restartActivityClass != null) {
            error_activity_restart_button!!.setText(R.string.btn_restart)
            error_activity_restart_button!!.setOnClickListener { CustomActivityOnCrash.restartApplication(this@CrashErrorActivity, config) }
        } else {
            error_activity_restart_button!!.setText(R.string.btn_close)
            error_activity_restart_button!!.setOnClickListener { CustomActivityOnCrash.closeApplication(this@CrashErrorActivity, config) }
        }


        if (config.isShowErrorDetails) {

            error_activity_more_info_button!!.setOnClickListener {
                //We retrieve all the error data and show it

                val dialog = AlertDialog.Builder(this@CrashErrorActivity)
                        .setTitle(R.string.customactivityoncrash_error_activity_error_details_title)
                        .setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(this@CrashErrorActivity, intent))
                        .setPositiveButton(R.string.customactivityoncrash_error_activity_error_details_close, null)
                        .setNeutralButton(R.string.customactivityoncrash_error_activity_error_details_copy
                        ) { _, _ ->
                            copyErrorToClipboard()
                            Toast.makeText(this@CrashErrorActivity,
                                    R.string.customactivityoncrash_error_activity_error_details_copied, Toast.LENGTH_SHORT).show()
                        }
                        .show()
                val textView = dialog.findViewById<TextView>(android.R.id.message) as TextView
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.customactivityoncrash_error_activity_error_details_text_size))
            }
        } else {
            error_activity_more_info_button.visibility = View.GONE
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun copyErrorToClipboard() {
        val errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(this@CrashErrorActivity, intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(getString(R.string.customactivityoncrash_error_activity_error_details_clipboard_label), errorInformation)
            clipboard.primaryClip = clip
        } else {

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
            clipboard.text = errorInformation
        }
    }

}