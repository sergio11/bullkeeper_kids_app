package com.sanchez.sanchez.bullkeeper_kids.core.navigation

import android.app.Activity
import android.app.Service
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

}