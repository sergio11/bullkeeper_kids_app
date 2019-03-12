package com.sanchez.sanchez.bullkeeper_kids.core.navigation

import android.app.Activity
import android.app.Service
import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.presentation.legalcontent.LegalContentActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.lockscreen.AppLockScreenActivity

/**
 * Navigator
 */
interface INavigator {

    /**
     * Show App Tutorial
     */
    fun showAppTutorial(activity: Activity)

    /**
     * Show Login
     */
    fun showLogin(activity: Activity)

    /**
     * Show Login
     */
    fun showLogin(service: Service)

    /**
     * Show Home
     */
    fun showHome(activity: Activity)

    /**
     * Show Main
     */
    fun showMain(activity: Activity)

    /**
     * Show Lock Screen
     */
    fun showLockScreen(service: Service, appLockType: AppLockScreenActivity.Companion.LockTypeEnum, packageName: String?, appName: String?, icon: String?, appRule: String?)

    /**
     * Show Scheduled Block Active
     */
    fun showScheduledBlockActive(ctx: Context, identity: String?, name: String?, image: String?, startAt: String?,
                                 endAt: String?, description: String?)

    /**
     * Show Disabled App Screen
     */
    fun showDisabledAppScreen(ctx: Context, packageName: String?, appName: String?, icon: String?, appRule: String?)

    /**
     * Show Enable
     */
    fun showEnableAdminDeviceFeatures(activity: Activity, resultCode: Int)

    /**
     * Show Enable
     */
    fun showEnableAdminDeviceFeatures(activity: Activity)

    /**
     * Show Usage Access Settings
     */
    fun showUsageAccessSettings(activity: Activity)

    /**
     * Show Legal Content
     */
    fun showLegalContent(activity: Activity, legalContentType: LegalContentActivity.LegalTypeEnum)

    /**
     * Show Link Terminal Tutorial
     */
    fun showLinkTerminalTutorial(activity: Activity)

    /**
     * Show SOS Screen
     */
    fun showSosScreen(activity: Activity)

    /**
     * Show Pick Me Up Scree
     */
    fun showPickMeUpScreen(activity: Activity)

    /**
     * Show Time Bank Screen
     */
    fun showTimeBankScreen(activity: Activity)

    /**
     * Show Bed Time Screen
     */
    fun showBedTimeScreen(ctx: Context)

    /**
     * Show Phone Number Blocked Screen
     */
    fun showPhoneNumberBlockedScreen(ctx: Context, phoneNumber: String, blockedAt: String)

    /**
     * Show Phone Number Blocked Screen
     */
    fun showPhoneNumberBlockedScreen(ctx: Context)

    /**
     * Show Settings Screen
     */
    fun showSettingsScreen(activity: Activity)

    /**
     * Show Geofence Violated Activity
     */
    fun showGeofenceViolatedActivity(ctx: Context, name: String?, type: String?, radius: Float?)

    /**
     * Show Settings Lock Screen Activity
     */
    fun showSettingsLockScreenActivity(ctx: Context)

    /**
     * Show Conversation Message List
     */
    fun showConversationMessageList(activity: Activity, conversation: String)

    /**
     * Show Conversation Message List
     */
    fun showConversationMessageList(activity: Activity, memberOne: String, memberTwo: String)

    /**
     * Show Conversation List
     */
    fun showConversationList(activity: Activity)

    /**
     * Show Kid Guardians
     */
    fun showKidGuardians(activity: Activity, requestCode: Int)

    /**
     * Show Location Source Settings
     * @param activity
     * @param requestCode
     */
    fun showLocationSourceSettings(activity: Activity)


}