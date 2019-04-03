package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.LinkDeviceTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.permission.IPermissionManager
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportPageFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.GetTerminalDetailInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.TerminalEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.ILinkDeviceTutorialHandler
import kotlinx.android.synthetic.main.first_link_terminal_page_fragment_layout.*
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

/**
 * First Link Terminal Page Fragment
 */
class FirstLinkTerminalPageFragment: SupportPageFragment<LinkDeviceTutorialComponent>() {

    /**
     * Dependencies
     * ===============
     */

    @Inject
    lateinit var firstLinkTerminalViewModel: FirstLinkTerminalViewModel

    @Inject
    lateinit var preferenceRepository: IPreferenceRepository

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
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create the observer which updates the UI.
        val terminalEntityObserver = Observer<TerminalEntity> { terminalEntity ->
            Timber.d("Terminal Already linked go to home")
            linkDeviceTutorialHandler.goToHome()
        }

        // Create the observer which updates the UI.
        val terminalEntityFailureObserver = Observer<Failure> { failure ->
            if( failure is FirstLinkTerminalViewModel.NoChildrenLinkedFailure ||
                    failure is GetTerminalDetailInteract.NoTerminalFoundFailure) {
                titleText.text = getString(R.string.link_terminal_first_page_title)
                contentText.visibility = VISIBLE
                linkDeviceTutorialHandler.hideProgressDialog()
            } else if (failure is Failure.UnauthorizedRequestError) {
                preferenceRepository.setPrefKidIdentity(IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE)
                preferenceRepository.setPrefTerminalIdentity(IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE)
                linkDeviceTutorialHandler.goToLogin()

            } else {
                linkDeviceTutorialHandler.goToHome()
            }

        }

        firstLinkTerminalViewModel.terminalSuccess.observe(this, terminalEntityObserver)
        firstLinkTerminalViewModel.terminalFailure.observe(this, terminalEntityFailureObserver)

        linkDeviceTutorialHandler.showProgressDialog(R.string.generic_loading_text)
        firstLinkTerminalViewModel.checkTerminalStatus()

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