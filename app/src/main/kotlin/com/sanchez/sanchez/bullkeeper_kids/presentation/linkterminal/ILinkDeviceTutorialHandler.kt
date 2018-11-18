package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial

import com.sanchez.sanchez.bullkeeper_kids.core.platform.IBasicActivityHandler

/**
 * Link Device Tutorial Handler
 */
interface ILinkDeviceTutorialHandler: IBasicActivityHandler {

    /**
     * Request
     */
    fun requestFocus()

    /**
     * Release Focus
     */
    fun releaseFocus()

}