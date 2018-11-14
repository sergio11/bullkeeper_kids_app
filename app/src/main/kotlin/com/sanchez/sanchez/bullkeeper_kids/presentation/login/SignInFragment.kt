package com.sanchez.sanchez.bullkeeper_kids.presentation.login

import android.content.Context
import android.os.Bundle
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.R
import kotlinx.android.synthetic.main.fragment_login.*
import java.lang.IllegalStateException

/**
 * Login Fragment
 */
class SignInFragment : BaseFragment() {

    /**
     * Sign In Activity Handler
     */
    lateinit var signInActivityHandler: ISignInActivityHandler

    /**
     * Layout Id
     */
    override fun layoutId() = R.layout.fragment_login

    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context !is ISignInActivityHandler)
            throw IllegalStateException("Context is not an instance of ISignInActivityHandler")

        signInActivityHandler = context
    }

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Login Btn
        loginBtn.setOnClickListener {
            signInActivityHandler.navigateToHome()
        }


    }
}
