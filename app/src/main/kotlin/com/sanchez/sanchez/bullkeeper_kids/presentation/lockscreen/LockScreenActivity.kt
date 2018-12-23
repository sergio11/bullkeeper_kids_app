package com.sanchez.sanchez.bullkeeper_kids.presentation.lockscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.sanchez.sanchez.bullkeeper_kids.R

import kotlinx.android.synthetic.main.activity_lock_screen.*
import android.content.BroadcastReceiver
import android.support.v4.content.LocalBroadcastManager
import android.content.IntentFilter


/**
 * Lock Screen Activity
 */
class LockScreenActivity : AppCompatActivity() {

    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context): Intent {
            val intent = Intent(context, LockScreenActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_REORDER_TO_FRONT )
            return intent
        }
    }

    var mLocalBroadcastManager: LocalBroadcastManager? = null
    var mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.sanchez.sergio.unlock") {
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
        setSupportActionBar(toolbar)
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction("com.sanchez.sergio.unlock")
        mLocalBroadcastManager?.registerReceiver(mBroadcastReceiver, mIntentFilter)

    }

    override fun onDestroy() {
        super.onDestroy()
        mLocalBroadcastManager?.unregisterReceiver(mBroadcastReceiver)
    }

}
