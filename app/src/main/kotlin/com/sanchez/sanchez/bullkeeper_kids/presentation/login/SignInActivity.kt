package com.sanchez.sanchez.bullkeeper_kids.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity
import javax.inject.Inject

/**
 * Sign In Activity
 */
class SignInActivity : BaseActivity(), ISignInActivityHandler {


    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    /**
     *
     */
    companion object {
        fun callingIntent(context: Context) =
                Intent(context, SignInActivity::class.java)
    }


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
        appComponent.inject(this)
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
        navigator.showHome(this)
    }



}
