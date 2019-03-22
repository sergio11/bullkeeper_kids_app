package com.sanchez.sanchez.bullkeeper_kids.presentation.login

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.SignInComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.account.AuthenticateInteract
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject



/**
 * SignIn Fragment
 */
class SignInFragment : BaseFragment(), FacebookCallback<LoginResult> {


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
     * CallBack Manager
     */
    private lateinit var callbackManager: CallbackManager

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
            signInActivityHandler.hideProgressDialog()
            signInActivityHandler.navigateToHome()
        }

        // Create the observer which updates the UI.
        val signInFailureObserver = Observer<Failure> { failure ->
            signInActivityHandler.hideProgressDialog()

            when(failure) {

                /**
                 * Incorrect Credentials
                 */
                is AuthenticateInteract.IncorrectCredentials -> {
                    signInActivityHandler.showNoticeDialog(R.string.authenticating_invalid_credentials, object : NoticeDialogFragment.NoticeDialogListener {
                        override fun onAccepted(dialog: DialogFragment) {
                            emailInput.text.clear()
                            emailInput.requestFocus()
                            passwordInput.text.clear()
                        }
                    })
                }

                else -> {

                    signInActivityHandler.showNoticeDialog(R.string.authenticating_error_ocurred, object : NoticeDialogFragment.NoticeDialogListener {
                        override fun onAccepted(dialog: DialogFragment) {
                            emailInput.text.clear()
                            emailInput.requestFocus()
                            passwordInput.text.clear()
                        }
                    })

                }
            }

        }


        signInViewModel.sigInSuccess.observe(this, signInSuccessObserver)
        signInViewModel.sigInFailure.observe(this, signInFailureObserver)

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

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(
                callbackManager, this)

        // Login Btn
        loginBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val  password = passwordInput.text.toString()
            if(!email.isEmpty() && !password.isEmpty()) {
                signInViewModel.authenticate(email, password)
                signInActivityHandler.showProgressDialog(R.string.authenticating_in_progress)
            } else {
                signInActivityHandler.showNoticeDialog(R.string.you_must_provide_email_and_password)
            }

        }

        // Login Facebook
        loginFacebook.setOnClickListener {

            val userReadPermissions = Arrays.asList("email", "user_birthday", "user_hometown",
                    "user_location", "public_profile")

            signInActivityHandler.showProgressDialog(R.string.authenticating_in_progress)

            LoginManager.getInstance()
                    .logInWithReadPermissions(this, userReadPermissions)

        }
    }


    /**
     * On Activity Result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * On Success
     * @param loginResult
     */
    override fun onSuccess(loginResult: LoginResult) {
        Timber.d("Facebook Login Success")
        val accessToken = loginResult.accessToken
        if (accessToken != null)
            signInViewModel.authenticate(accessToken)
        else
            signInActivityHandler.showShortMessage(R.string.authenticating_with_facebook_error)
    }

    /**
     * On Cancel
     */
    override fun onCancel() {
        Timber.d("Facebook Login Canceled")
        signInActivityHandler.hideProgressDialog()
        signInActivityHandler.showShortMessage(R.string.authenticating_with_facebook_canceled)
    }

    /**
     * On Error
     * @param error
     */
    override fun onError(error: FacebookException) {
        Timber.d("Facebook Login Exception")
        signInActivityHandler.showShortMessage(R.string.authenticating_with_facebook_error)
    }
}
