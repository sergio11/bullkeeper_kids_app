package com.sanchez.sanchez.bullkeeper_kids.core.navigation

import android.app.Activity
import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.presentation.legalcontent.LegalContentActivity

/**
 * Navigator
 */
interface INavigator {

    /**
     * Show App Tutorial
     */
    fun showAppTutorial(context: Context)

    /**
     * Show Login
     */
    fun showLogin(context: Context)

    /**
     * Show Home
     */
    fun showHome(context: Context)

    /**
     * Show Main
     */
    fun showMain(context: Context)

    /**
     * Show Lock Screen
     */
    fun showLockScreen(context: Context)

    /**
     * Show Enable
     */
    fun showEnableAdminDeviceFeatures(activity: Activity, resultCode: Int)

    /**
     * Show Legal Content
     */
    fun showLegalContent(context: Context, legalContentType: LegalContentActivity.LegalTypeEnum)

}