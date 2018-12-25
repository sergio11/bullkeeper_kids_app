package com.sanchez.sanchez.bullkeeper_kids.presentation.sos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.DaggerSosComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.SosComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import kotlinx.android.synthetic.main.sos_toolbar.*
import javax.inject.Inject

/**
 * Sos Activity
 */
class SosActivity : BaseActivity(),
        HasComponent<SosComponent>, ISosActivityHandler {


    /**
     * Sos Component
     */
    private val sosComponent: SosComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerSosComponent
                .builder()
                .applicationComponent((application as AndroidApplication).appComponent)
                .activityModule(ActivityModule(this))
                .build()
    }


    val TAG = "SOS_ACTIVITY"

    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context) = Intent(context, SosActivity::class.java)
    }

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator : INavigator

    /**
     * Component
     */
    override val component: SosComponent
        get() = sosComponent

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sosComponent.inject(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        backIcon.setOnClickListener{
            navigator.showHome(this)
        }

    }


    /**
     * Get Layout Resource
     */
    override fun getLayoutRes(): Int = R.layout.activity_sos

    /**
     * Fragment
     */
    override fun fragment(): BaseFragment = SosActivityFragment()

}
