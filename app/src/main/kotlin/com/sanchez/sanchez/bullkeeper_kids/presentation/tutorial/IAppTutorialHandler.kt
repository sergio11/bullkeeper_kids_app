package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial

import com.sanchez.sanchez.bullkeeper_kids.core.platform.IBasicActivityHandler
import com.sanchez.sanchez.bullkeeper_kids.presentation.legalcontent.LegalContentActivity

/**
 * App Tutorial Handler
 */
interface IAppTutorialHandler: IBasicActivityHandler {

    /**
     * Show Legal Content
     */
    fun showLegalContent(legalContent: LegalContentActivity.LegalTypeEnum)

    /**
     * Request
     */
    fun requestFocus()

    /**
     * Release Focus
     */
    fun releaseFocus()

    /**
     * Show Usage Access Settings
     */
    fun showUsageAccessSettings()

    /**
     * Show SignIn
     */
    fun showSignIn()

    /**
     * Show Device Admin Settings
     */
    fun showDeviceAdminSettings()

    /**
     * Show Location Source Settings
     */
    fun showLocationSourceSettings()

    /**
     * Show Manage Overlay Settings
     */
    fun showManageOverlaySettings()
}