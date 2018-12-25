package com.sanchez.sanchez.bullkeeper_kids.presentation.pickmeup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.DaggerPickMeUpComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.PickMeUpComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import kotlinx.android.synthetic.main.sos_toolbar.*
import javax.inject.Inject

/**
 * Pick Me Up Activity
 */
class PickMeUpActivity : BaseActivity(),
        HasComponent<PickMeUpComponent>, IPickMeUpActivityHandler {


    /**
     * Pick Me Up Component
     */
    private val pickMeUpComponent: PickMeUpComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerPickMeUpComponent
                .builder()
                .applicationComponent((application as AndroidApplication).appComponent)
                .activityModule(ActivityModule(this))
                .build()
    }


    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context) = Intent(context, PickMeUpActivity::class.java)
    }

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator : INavigator

    /**
     * Component
     */
    override val component: PickMeUpComponent
        get() = pickMeUpComponent

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMeUpComponent.inject(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        backIcon.setOnClickListener{
            navigator.showHome(this)
        }

    }


    /**
     * Get Layout Resource
     */
    override fun getLayoutRes(): Int = R.layout.activity_pick_me_up

    /**
     * Fragment
     */
    override fun fragment(): BaseFragment = PickMeUpActivityFragment()

}
