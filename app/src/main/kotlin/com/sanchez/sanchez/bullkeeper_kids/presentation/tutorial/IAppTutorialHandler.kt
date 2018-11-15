package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial

import com.sanchez.sanchez.bullkeeper_kids.presentation.legalcontent.LegalContentActivity

/**
 * App Tutorial Handler
 */
interface IAppTutorialHandler {

    /**
     * Show Legal Content
     */
    fun showLegalContent(legalContent: LegalContentActivity.LegalTypeEnum)
}