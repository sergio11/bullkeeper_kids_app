package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial

import com.sanchez.sanchez.bullkeeper_kids.core.platform.IBasicActivityHandler
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SonEntity

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

    /**
     * Set Current Son Entity
     */
    fun setCurrentSonEntity(sonEntity: SonEntity?)

    /**
     * Get Current Son Entity
     */
    fun getCurrentSonEntity(): SonEntity?

    /**
     * Has Current Son Entity
     */
    fun hasCurrentSonEntity(): Boolean

    /**
     * Go To Home
     */
    fun goToHome()

}