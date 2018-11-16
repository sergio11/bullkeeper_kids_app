package com.sanchez.sanchez.bullkeeper_kids.core.navigation.impl

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.MonitoringDeviceAdminReceiver
import com.sanchez.sanchez.bullkeeper_kids.presentation.home.HomeActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.legalcontent.LegalContentActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.lockscreen.LockScreenActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.login.SignInActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.AppTutorialActivity
import com.sanchez.sanchez.bullkeeper_kids.services.IAuthenticatorService
import javax.inject.Inject

/**
 * Navigator Impl
 */
class NavigatorImpl
    @Inject constructor(private val authenticatorService: IAuthenticatorService): INavigator {

    /**
     * Show App Tutorial
     */
    override fun showAppTutorial(context: Context) =
            context.startActivity(AppTutorialActivity.callingIntent(context))

    /**
     * Show Login
     */
    override fun showLogin(context: Context) =
            context.startActivity(SignInActivity.callingIntent(context))

    /**
     * Show Home
     */
    override fun showHome(context: Context) =
            context.startActivity(HomeActivity.callingIntent(context))

    /**
     * Show Main
     */
    override fun showMain(context: Context) {
        when (authenticatorService.userLoggedIn()) {
            true -> showHome(context)
            false -> showLogin(context)
        }
    }

    /**
     * Show Lock Screen
     */
    override fun showLockScreen(context: Context) =
            context.startActivity(LockScreenActivity.callingIntent(context))

    /**
     * Show Enable Admin Device Features
     */
    override fun showEnableAdminDeviceFeatures(activity: Activity, resultCode: Int) {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                MonitoringDeviceAdminReceiver.getComponentName(activity))
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Additional text explaining why we need this permission")
        activity.startActivityForResult(intent, resultCode)
    }


    /**
     * Show Legal Content
     */
    override fun showLegalContent(context: Context, legalContentType: LegalContentActivity.LegalTypeEnum)
        = context.startActivity(LegalContentActivity.callingIntent(context = context,
            legalTypeEnum = legalContentType))

}