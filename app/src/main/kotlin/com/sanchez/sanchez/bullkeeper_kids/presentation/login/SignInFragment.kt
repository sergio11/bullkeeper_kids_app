package com.sanchez.sanchez.bullkeeper_kids.presentation.login

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.SignInComponent
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

/**
 * SignIn Fragment
 */
class SignInFragment : BaseFragment() {

    /**
     * Sign In Activity Handler
     */
    lateinit var signInActivityHandler: ISignInActivityHandler

    /**
     * Sign In View Model
     */
    @Inject
    lateinit var signInViewModel: SignInViewModel

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
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeInjector()

        // Create the observer which updates the UI.
        val signInSuccessObserver = Observer<SignInSuccessView> { signInSuccessView ->
            Timber.d("Auth Token -> %s", signInSuccessView?.token)
            signInActivityHandler.navigateToHome()
        }

        signInViewModel.sigInSuccess.observe(this, signInSuccessObserver)

    }

    /**
     * Initialize Injector
     */
    fun initializeInjector() {
        val signInComponent = SignInComponent::class.java
                .cast((activity as HasComponent<SignInComponent>)
                        .component)

        signInComponent.inject(this)
    }

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Login Btn
        loginBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val  password = passwordInput.text.toString()
            signInViewModel.authenticate(email, password)
        }

    }
}
