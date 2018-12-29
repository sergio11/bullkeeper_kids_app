package com.sanchez.sanchez.bullkeeper_kids.core.navigation

import android.app.Activity
import android.app.Service
import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.presentation.legalcontent.LegalContentActivity

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
    fun showLockScreen(service: Service, packageName: String?, appName: String?, icon: String?, appRule: String?)

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

}