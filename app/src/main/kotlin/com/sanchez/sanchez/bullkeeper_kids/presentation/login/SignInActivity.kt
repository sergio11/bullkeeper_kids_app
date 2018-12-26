package com.sanchez.sanchez.bullkeeper_kids.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.DaggerSignInComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.SignInComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.AuthModule
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity
import javax.inject.Inject

/**
 * Sign In Activity
 */
class SignInActivity : BaseActivity(), ISignInActivityHandler,
    HasComponent<SignInComponent>{



    /**
     * Sign In Component
     */
    private val signInComponent: SignInComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerSignInComponent
                .builder()
                .applicationComponent((application as AndroidApplication).appComponent)
                .activityModule(ActivityModule(this))
                .authModule(AuthModule())
                .build()
    }

    /**
     *
     */
    companion object {
        fun callingIntent(context: Context): Intent {
            val intent = Intent(context, SignInActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }
    }

    /**
     * Get Component
     */
    override val component: SignInComponent
        get() = signInComponent

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
        signInComponent.inject(this)
    }

    /**
     * Get Fragment
     */
    override fun fragment() = SignInFragment()

    /**
     * Get Layout Id
     */
    override fun getLayoutRes(): Int = R.layout.activity_login

    /**
     * Navigate To Home
     */
    override fun navigateToHome() {
        navigator.showLinkTerminalTutorial(this)
    }

}
