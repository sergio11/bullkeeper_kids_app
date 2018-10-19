package com.sanchez.sanchez.bullkeeper_kids.core.navigation

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.services.AuthenticatorService
import com.sanchez.sanchez.bullkeeper_kids.presentation.login.LoginActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.home.HomeActivity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Navigator
 */
@Singleton
class Navigator
    @Inject constructor(private val authenticatorService: AuthenticatorService) {

    /**
     * Show Login
     */
    private fun showLogin(context: Context) =
            context.startActivity(LoginActivity.callingIntent(context))

    /**
     * Show Main
     */
    fun showMain(context: Context) {
        when (authenticatorService.userLoggedIn()) {
            true -> showHome(context)
            false -> showLogin(context)
        }
    }

    /**
     * Show Home
     */
    private fun showHome(context: Context) =
            context.startActivity(HomeActivity.callingIntent(context))

}


