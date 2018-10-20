package com.sanchez.sanchez.bullkeeper_kids.presentation.lockscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.sanchez.sanchez.bullkeeper_kids.R

import kotlinx.android.synthetic.main.activity_lock_screen.*

/**
 * Lock Screen Activity
 */
class LockScreenActivity : AppCompatActivity() {

    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context) = Intent(context, LockScreenActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_screen)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
