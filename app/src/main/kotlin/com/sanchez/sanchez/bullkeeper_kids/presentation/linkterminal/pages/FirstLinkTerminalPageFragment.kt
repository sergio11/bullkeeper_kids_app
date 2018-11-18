package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.content.Context
import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.LinkDeviceTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportPageFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.ILinkDeviceTutorialHandler
import timber.log.Timber
import java.lang.IllegalStateException

/**
 * First Link Terminal Page Fragment
 */
class FirstLinkTerminalPageFragment: SupportPageFragment<LinkDeviceTutorialComponent>() {

    /**
     * Link Device Tutorial Handler
     */
    lateinit var linkDeviceTutorialHandler: ILinkDeviceTutorialHandler

    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int = R.layout.first_link_terminal_page_fragment_layout

    /**
     * Initialize Injector
     */
    override fun initializeInjector(): LinkDeviceTutorialComponent {
        val linkDeviceTutorialComponent =
                LinkDeviceTutorialComponent::class.java
                .cast((activity as HasComponent<LinkDeviceTutorialComponent>)
                        .component)
        linkDeviceTutorialComponent.inject(this)
        return linkDeviceTutorialComponent
    }

    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        /**
         * Check Context
         */
        if(context !is ILinkDeviceTutorialHandler)
            throw IllegalStateException("The context does not implement the handler ILinkDeviceTutorialHandler")

        linkDeviceTutorialHandler = context

    }

    /**
     * When Phase Is Hidden
     */
    override fun whenPhaseIsHidden(pagePosition: Int, currentPosition: Int) {
        Timber.d("Phase Is Hidden")
    }

    /**
     * When Phase Is Showed
     */
    override fun whenPhaseIsShowed() {
        Timber.d("Phase Is Showed")
    }


    /**
     * Get Transform Items
     */
    override fun getTransformItems(): Array<TransformItem> {
        return arrayOf(
                TransformItem.create(R.id.titleText, Direction.LEFT_TO_RIGHT, 0.2f),
                TransformItem.create(R.id.appIcon, Direction.RIGHT_TO_LEFT, 0.7f),
                TransformItem.create(R.id.contentText, Direction.LEFT_TO_RIGHT, 0.7f)
        )
    }

}