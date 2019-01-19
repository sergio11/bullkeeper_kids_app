package com.sanchez.sanchez.bullkeeper_kids.presentation.settings

import com.sanchez.sanchez.bullkeeper_kids.core.platform.IBasicActivityHandler

/**
 * Settings Activity Handler
 */
interface ISettingsActivityHandler: IBasicActivityHandler {

    /**
     * Show Usage Access Settings
     */
    fun showUsageAccessSettings()
    /**
     * Show Device Admin Settings
     */
    fun showDeviceAdminSettings()
}