package com.sanchez.sanchez.bullkeeper_kids.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.SynchronizeInstalledPackagesInteract
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

/**
 * Home Activity
 */
class HomeActivity : AppCompatActivity() {

    val TAG = "HOME_ACTIVITY"

    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    /**
     * System Package Helper Impl
     */
    @Inject
    internal lateinit var synchronizeInstalledPackagesInteract: SynchronizeInstalledPackagesInteract


    /**
     * Handle Failure
     */
    protected fun handleFailure(failure: Failure) {
        Log.d(TAG, "Handle Failure")
    }

    private fun handleSuccess(unit: Unit) {
       Log.d(TAG, "Sync Success")
    }

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        appComponent.inject(this)

        synchronizeInstalledPackagesInteract(UseCase.None()){
            it.either(::handleFailure, ::handleSuccess)
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
