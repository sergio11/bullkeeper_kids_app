package com.sanchez.sanchez.bullkeeper_kids.core.navigation

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.MonitoringDeviceAdminReceiver
import com.sanchez.sanchez.bullkeeper_kids.services.AuthenticatorService
import com.sanchez.sanchez.bullkeeper_kids.presentation.login.SignInActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.home.HomeActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.lockscreen.LockScreenActivity
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
    fun showLogin(context: Context) =
            context.startActivity(SignInActivity.callingIntent(context))

    /**
     * Show Home
     */
     fun showHome(context: Context) =
            context.startActivity(HomeActivity.callingIntent(context))

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
     * Show Lock Screen
     */
    fun showLockScreen(context: Context) =
            context.startActivity(LockScreenActivity.callingIntent(context))


    /**
     * Show Enable Admin Device Features
     */
    fun showEnableAdminDeviceFeatures(activity: Activity, resultCode: Int) = {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                MonitoringDeviceAdminReceiver.getComponentName(activity))
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Additional text explaining why we need this permission")
        activity.startActivityForResult(intent, resultCode)
    }




}


