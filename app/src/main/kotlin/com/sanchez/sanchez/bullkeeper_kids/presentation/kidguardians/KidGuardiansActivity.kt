package com.sanchez.sanchez.bullkeeper_kids.presentation.kidguardians

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.DaggerKidGuardianComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.KidGuardianComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ChildrenModule
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.models.KidGuardianEntity
import kotlinx.android.synthetic.main.app_translucent_toolbar_return.*
import javax.inject.Inject


/**
 * Kid Guardians Activity
 */
class KidGuardiansActivity : BaseActivity(),
        HasComponent<KidGuardianComponent>, IKidGuardiansListHandler {

    /**
     * Kid Guardian Component
     */
    private val kidGuardianComponent: KidGuardianComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerKidGuardianComponent
                .builder()
                .applicationComponent((application as AndroidApplication).appComponent)
                .activityModule(ActivityModule(this))
                .childrenModule(ChildrenModule())
                .build()
    }


    /**
     * Kid Guardian
     */
    override val component: KidGuardianComponent
        get() = kidGuardianComponent


    /**
     * Dependencies
     * =================
     */

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: INavigator


    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kidGuardianComponent.inject(this)

        backIcon.setOnClickListener {
            onBackPressed()
        }

        appTitle.setOnClickListener {
            navigator.showHome(this)
        }
    }

    /**
     * Get Layout Resource
     */
    override fun getLayoutRes(): Int = R.layout.activity_kid_guardian_list


    /**
     * Fragment
     */
    override fun fragment(): BaseFragment = KidGuardiansActivityFragment()

    /**
     * On Kid Guardian Selected
     */
    override fun onKidGuardianSelected(kidGuardian: KidGuardianEntity) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtras(Bundle().apply {
                putSerializable(KID_GUARDIAN_SELECTED_ARG, kidGuardian)
            })
        })
        finish()
    }

    companion object {

        /**
         * Kid Guardian Selected
         */
        const val KID_GUARDIAN_SELECTED_ARG = "KID_GUARDIAN_SELECTED_ARG"

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context): Intent
                = Intent(context, KidGuardiansActivity::class.java)
    }

}
