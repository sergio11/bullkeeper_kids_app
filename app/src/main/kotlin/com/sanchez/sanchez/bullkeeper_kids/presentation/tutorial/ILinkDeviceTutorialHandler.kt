package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial

import com.sanchez.sanchez.bullkeeper_kids.core.platform.IBasicActivityHandler
import com.sanchez.sanchez.bullkeeper_kids.domain.models.KidEntity

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
     * Set Current Kid Entity
     */
    fun setCurrentKidEntity(kidEntity: KidEntity?)

    /**
     * Get Current Kid Entity
     */
    fun getCurrentKidEntity(): KidEntity?

    /**
     * Has Current Kid Entity
     */
    fun hasCurrentKidEntity(): Boolean

    /**
     * Go To Home
     */
    fun goToHome()

}