package com.sanchez.sanchez.bullkeeper_kids.presentation.bedtime

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
import javax.inject.Inject

/**
 * Bed Time Activity
 */
class BedTimeActivity : BaseActivity(),
        IBedTimeActivityHandler, HasComponent<ApplicationComponent> {


    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: INavigator


    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context) = Intent(context, BedTimeActivity::class.java)
    }


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
    }


    /**
     * Get Layout Resource
     */
    override fun getLayoutRes(): Int = R.layout.activity_bed_time

    /**
     * Fragment
     */
    override fun fragment(): BaseFragment = BedTimeActivityFragment()

    /**
     * Show SOS Screen
     */
    override fun showSosScreen() = navigator.showSosScreen(this)

    /**
     * Show Pick Me Up Screen
     */
    override fun showPickMeUpScreen() = navigator.showPickMeUpScreen(this)
}
