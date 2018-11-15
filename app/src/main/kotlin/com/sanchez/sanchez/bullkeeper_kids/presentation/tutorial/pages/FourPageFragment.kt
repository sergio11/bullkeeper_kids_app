package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages

import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.PageSupportFragment
import com.cleveroad.slidingtutorial.TransformItem
import com.sanchez.sanchez.bullkeeper_kids.R

/**
 * Four Page Fragment
 */
class FourPageFragment: PageSupportFragment() {

    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int = R.layout.four_page_fragment_layout

    /**
     * Get Transform Items
     */
    override fun getTransformItems(): Array<TransformItem> {
        return arrayOf(
                TransformItem.create(R.id.titleText, Direction.LEFT_TO_RIGHT, 0.2f),
                TransformItem.create(R.id.runOnStartup, Direction.RIGHT_TO_LEFT, 0.07f),
                TransformItem.create(R.id.runInTheBackground, Direction.LEFT_TO_RIGHT, 0.07f)
        )
    }
}