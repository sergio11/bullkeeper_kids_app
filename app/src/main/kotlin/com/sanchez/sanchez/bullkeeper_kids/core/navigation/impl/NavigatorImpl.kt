package com.sanchez.sanchez.bullkeeper_kids.core.navigation.impl

import android.app.Activity
import android.app.Service
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.bedtime.BedTimeActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.MonitoringDeviceAdminReceiver
import com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.chat.ConversationMessageListActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.list.ConversationListActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.home.HomeActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.legalcontent.LegalContentActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.login.SignInActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.LinkDeviceTutorialActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.lockscreen.*
import com.sanchez.sanchez.bullkeeper_kids.presentation.phonenumberblocked.PhoneNumberBlockedActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.pickmeup.PickMeUpActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.settings.SettingsActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.sos.SosActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.timebank.TimeBankActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.AppTutorialActivity
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
        when (preferenceRepository.isTutorialCompleted()) {
            true -> activity.startActivity(SignInActivity.callingIntent(activity))
            false -> showAppTutorial(activity)
        }
    }

    /**
     * Show Login
     */
    override fun showLogin(service: Service) =
            service.startActivity(SignInActivity.callingIntent(service))

    /**
     * Show Home
     */
    override fun showHome(activity: Activity) {
        activity.startActivity(
                HomeActivity.callingIntent(activity))
        activity.finish()
    }


    /**
     * Show Main
     */
    override fun showMain(activity: Activity) {
        when (!preferenceRepository.getAuthToken().isNullOrEmpty()) {
            true -> showLinkTerminalTutorial(activity)
            false -> showLogin(activity)
        }
    }

    /**
     * Show Lock Screen
     */
    override fun showLockScreen(service: Service, appLockType: AppLockScreenActivity.Companion.LockTypeEnum, packageName: String?, appName: String?,
                                icon: String?, appRule: String?) =
            service.startActivity(
                    AppLockScreenActivity.callingIntent(service, appLockType, packageName, appName, icon, appRule))

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
    override fun showLegalContent(activity: Activity, legalContentType: LegalContentActivity.LegalTypeEnum) = activity.startActivity(LegalContentActivity.callingIntent(context = activity,
            legalTypeEnum = legalContentType))


    /**
     * Show Link Terminal Tutorial
     */
    override fun showLinkTerminalTutorial(activity: Activity) =
            activity.startActivity(LinkDeviceTutorialActivity.callingIntent(activity))


    /**
     * Show Sos Screen
     */
    override fun showSosScreen(activity: Activity) =
            activity.startActivity(SosActivity.callingIntent(activity))


    /**
     * Show Pick Me Up Screen
     */
    override fun showPickMeUpScreen(activity: Activity) =
            activity.startActivity(PickMeUpActivity.callingIntent(activity))

    /**
     * Show Time Bank Screen
     */
    override fun showTimeBankScreen(activity: Activity) =
            activity.startActivity(TimeBankActivity.callingIntent(activity))

    /**
     * Show Bed Time
     */
    override fun showBedTimeScreen(ctx: Context) =
            ctx.startActivity(BedTimeActivity.callingIntent(ctx))

    /**
     * Show Phone Number Blocked Screen
     */
    override fun showPhoneNumberBlockedScreen(ctx: Context, phoneNumber: String, blockedAt: String) =
            ctx.startActivity(PhoneNumberBlockedActivity.callingIntent(ctx, phoneNumber, blockedAt))

    /**
     * Show Phone Number Blocked Screen
     */
    override fun showPhoneNumberBlockedScreen(ctx: Context) =
            ctx.startActivity(PhoneNumberBlockedActivity.callingIntent(ctx))

    /**
     * Show Disabled App Screen
     */
    override fun showDisabledAppScreen(ctx: Context, packageName: String?,
                                       appName: String?, icon: String?, appRule: String?) =
            ctx.startActivity(DisabledAppScreenActivity.callingIntent(ctx, packageName, appName, icon, appRule))


    /**
     * Show Scheduled Block Active
     */
    override fun showScheduledBlockActive(ctx: Context, identity: String?, name: String?, image: String?,
                                          startAt: String?, endAt: String?,
                                          description: String?) = ctx.startActivity(ScheduledBlockActiveScreenActivity.callingIntent(ctx,
            identity, name, image, startAt, endAt, description))


    /**
     * Show Settings Screen
     */
    override fun showSettingsScreen(activity: Activity) =
            activity.startActivity(SettingsActivity.callingIntent(activity))

    /**
     * Show Geofence Violated Activity
     */
    override fun showGeofenceViolatedActivity(ctx: Context, name: String?,
                                              type: String?, radius: Float?) = ctx.startActivity(GeofenceViolatedActivity.callingIntent(
            ctx, name, type, radius))

    /**
     * Show Settings Lock Screen Activity
     */
    override fun showSettingsLockScreenActivity(ctx: Context) =
            ctx.startActivity(SettingsLockScreenActivity.callingIntent(ctx))

    /**
     * Show Conversation Message List
     */
    override fun showConversationMessageList(activity: Activity, conversation: String)
        = activity.startActivity(ConversationMessageListActivity.callingIntent(activity, conversation))

    /**
     * Show Conversation Message List
     */
    override fun showConversationMessageList(activity: Activity, memberOne: String, memberTwo: String)
        = activity.startActivity(ConversationMessageListActivity.callingIntent(activity, memberOne, memberTwo))


    /**
     * Show Conversation List
     */
    override fun showConversationList(activity: Activity) =
        activity.startActivity(ConversationListActivity.callingIntent(activity))
}