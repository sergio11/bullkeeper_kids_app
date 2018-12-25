package com.sanchez.sanchez.bullkeeper_kids.presentation.timebank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import kotlinx.android.synthetic.main.sos_toolbar.*
import javax.inject.Inject

/**
 * Time Bank Activity
 */
class TimeBankActivity : BaseActivity(), HasComponent<ApplicationComponent> {


    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }


    val TAG = "TIME_BANK_ACTIVITY"

    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context) = Intent(context, TimeBankActivity::class.java)
    }

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator : INavigator

    /**
     * Component
     */
    override val component: ApplicationComponent
        get() = appComponent

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        backIcon.setOnClickListener{
            navigator.showHome(this)
        }

    }


    /**
     * Get Layout Resource
     */
    override fun getLayoutRes(): Int = R.layout.activity_time_bank

    /**
     * Fragment
     */
    override fun fragment(): BaseFragment = TimeBankUpActivityFragment()

}
