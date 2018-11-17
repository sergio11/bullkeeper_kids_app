package com.sanchez.sanchez.bullkeeper_kids.core.navigation.impl

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.provider.Settings
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
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
    @Inject constructor(private val preferenceRepository: IPreferenceRepository): INavigator {

    /**
     * Show Usage Access Settings
     */
    override fun showUsageAccessSettings(activity: Activity) =
            activity.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))


    /**
     * Show App Tutorial
     */
    override fun showAppTutorial(activity: Activity) =
            activity.startActivity(AppTutorialActivity.callingIntent(activity))

    /**
     * Show Login
     */
    override fun showLogin(activity: Activity) {
        when(preferenceRepository.isTutorialCompleted()) {
            true -> activity.startActivity(SignInActivity.callingIntent(activity))
            false -> showAppTutorial(activity)
        }
    }

    /**
     * Show Home
     */
    override fun showHome(activity: Activity) =
            activity.startActivity(HomeActivity.callingIntent(activity))

    /**
     * Show Main
     */
    override fun showMain(activity: Activity) {
        when (false) {
            true -> showHome(activity)
            false -> showLogin(activity)
        }
    }

    /**
     * Show Lock Screen
     */
    override fun showLockScreen(activity: Activity) =
            activity.startActivity(LockScreenActivity.callingIntent(activity))

    /**
     * Show Enable Admin Device Features
     */
    override fun showEnableAdminDeviceFeatures(activity: Activity, resultCode: Int) {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                MonitoringDeviceAdminReceiver.getComponentName(activity))
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                activity.getString(R.string.required_administration_permissions))
        activity.startActivityForResult(intent, resultCode)
    }

    /**
     * Show Enable Admin Device Features
     */
    override fun showEnableAdminDeviceFeatures(activity: Activity) {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                MonitoringDeviceAdminReceiver.getComponentName(activity))
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                activity.getString(R.string.required_administration_permissions))
        activity.startActivity(intent)
    }

    /**
     * Show Legal Content
     */
    override fun showLegalContent(activity: Activity, legalContentType: LegalContentActivity.LegalTypeEnum)
        = activity.startActivity(LegalContentActivity.callingIntent(context = activity,
            legalTypeEnum = legalContentType))

}