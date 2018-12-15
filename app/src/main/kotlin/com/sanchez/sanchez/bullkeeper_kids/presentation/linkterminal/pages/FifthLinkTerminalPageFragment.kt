package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.LinkDeviceTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportPageFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.ILinkDeviceTutorialHandler
import kotlinx.android.synthetic.main.fifth_link_terminal_page_fragment_layout.*
import timber.log.Timber
import java.lang.IllegalStateException


/**
 * Fifth Link Terminal Page Fragment
 */
class FifthLinkTerminalPageFragment: SupportPageFragment<LinkDeviceTutorialComponent>() {

    /**
     * Link Device Tutorial Handler
     */
    lateinit var linkDeviceTutorialHandler: ILinkDeviceTutorialHandler

    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int
            = R.layout.fifth_link_terminal_page_fragment_layout

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
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        finish.setOnClickListener { linkDeviceTutorialHandler.goToHome() }
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
    @SuppressLint("SetTextI18n", "HardwareIds")
    override fun whenPhaseIsShowed() {
        Timber.d("Phase Is Showed")
    }


    /**
     * Get Transform Items
     */
    override fun getTransformItems(): Array<TransformItem> {
        return arrayOf(
                TransformItem.create(R.id.titleText,
                        Direction.LEFT_TO_RIGHT, 0.2f)
        )
    }

}