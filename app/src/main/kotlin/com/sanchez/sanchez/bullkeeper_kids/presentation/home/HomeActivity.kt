package com.sanchez.sanchez.bullkeeper_kids.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
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


    @Inject
    internal lateinit var usageStatsService: IUsageStatsService

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        appComponent.inject(this)

        if(!usageStatsService.isUsageStatsAllowed())
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
