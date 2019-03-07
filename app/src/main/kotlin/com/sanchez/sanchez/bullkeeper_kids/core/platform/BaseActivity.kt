package com.sanchez.sanchez.bullkeeper_kids.core.platform

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.extension.inTransaction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar.toolbar
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Base Activity class with helper methods for handling fragment transactions and back button
 * events.
 *
 * @see AppCompatActivity
 */
abstract class BaseActivity : SupportActivity() {

    /**
     * Last Connectivity
     */
    var lastConnectivity: Connectivity? = null


    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        setSupportActionBar(toolbar)
        addFragment(savedInstanceState)

        ReactiveNetwork
                .observeNetworkConnectivity(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{conectivity ->
                    lastConnectivity = conectivity
                    if(conectivity.available())
                        onConnectivityAvailable()
                    else
                        onConnectivityNotAvailable()
                }
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

    /**
     * On Connectivity Available
     */
    open fun onConnectivityAvailable(){}

    /**
     * On Connectivity Not Available
     */
    open fun onConnectivityNotAvailable(){}
}
