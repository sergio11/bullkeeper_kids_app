package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages

import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.AppTutorialComponent
import timber.log.Timber

/**
 * Four Page Fragment
 */
class FourPageFragment: AbstractPageFragment<AppTutorialComponent>() {

    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int = R.layout.four_page_fragment_layout

    /**
     * When Phase Is Hidden
     */
    override fun whenPhaseIsHidden(pagePosition: Int, currentPosition: Int) {
        Timber.d("When Phase Is Hidden")
    }

    /**
     * When Phase Is Showed
     */
    override fun whenPhaseIsShowed() {
        Timber.d("When Phase Is Showed")
    }

    /**
     * Initialize Injector
     */
    override fun initializeInjector(): AppTutorialComponent? {
        val appTutorialComponent = AppTutorialComponent::class.java
                .cast((activity as HasComponent<AppTutorialComponent>)
                        .component)
        appTutorialComponent.inject(this)
        return appTutorialComponent
    }

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