package com.sanchez.sanchez.bullkeeper_kids.core.platform

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.extension.inTransaction
import kotlinx.android.synthetic.main.toolbar.toolbar
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Base Activity class with helper methods for handling fragment transactions and back button
 * events.
 *
 * @see AppCompatActivity
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        setSupportActionBar(toolbar)
        addFragment(savedInstanceState)
    }

    /**
     * Attach Base Context
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    /**
     * On Back Pressed
     */
    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(
                R.id.fragmentContainer) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }

    /**
     * Add Fragment
     */
    private fun addFragment(savedInstanceState: Bundle?) =
            savedInstanceState ?: supportFragmentManager.inTransaction { add(
                    R.id.fragmentContainer, fragment()) }

    /**
     * Fragment
     */
    abstract fun fragment(): BaseFragment


    /**
     * Get Layout Resource
     */
    @LayoutRes
    abstract fun getLayoutRes(): Int
}
