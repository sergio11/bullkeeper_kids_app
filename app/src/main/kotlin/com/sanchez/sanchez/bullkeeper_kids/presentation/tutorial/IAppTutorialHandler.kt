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
}