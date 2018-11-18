package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal

import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.cleveroad.slidingtutorial.TutorialPageProvider
import com.cleveroad.slidingtutorial.TutorialSupportFragment
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.DaggerLinkDeviceTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.LinkDeviceTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.LinkTerminalModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ParentModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.TerminalModule
import com.sanchez.sanchez.bullkeeper_kids.core.extension.addFragment
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportActivity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SonEntity
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages.FirstLinkTerminalPageFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages.SecondLinkTerminalPageFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages.ThirdLinkTerminalPageFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.ILinkDeviceTutorialHandler
import timber.log.Timber
import javax.inject.Inject

/**
 * Link Device Tutorial Activity
 */
class LinkDeviceTutorialActivity : SupportActivity(), ILinkDeviceTutorialHandler,
        HasComponent<LinkDeviceTutorialComponent> {

    /**
     * Page Position
     */
    private val FIRST_PAGE_POS = 0
    private val SECOND_PAGE_POS = 1
    private val THIRD_PAGE_POS = 2
    private val TUTORIAL_PAGES_COUNT = 3

    /**
     * App Tutorial Component
     */
    private val linkDeviceTutorialComponent: LinkDeviceTutorialComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerLinkDeviceTutorialComponent
                .builder()
                .applicationComponent((application as AndroidApplication).appComponent)
                .activityModule(ActivityModule(this))
                .linkTerminalModule(LinkTerminalModule())
                .parentModule(ParentModule(application as AndroidApplication))
                .terminalModule(TerminalModule(application as AndroidApplication))
                .build()
    }

    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context) =
                Intent(context, LinkDeviceTutorialActivity::class.java)
    }


    /**
     * Dependencies
     * ====================
     */

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: INavigator

    /**
     * Tutorial Support Fragment
     */
    private lateinit var tutorialSupportFragment: TutorialSupportFragment

    /**
     * Current Son Entity
     */
    private var currentSonEntity: SonEntity? = null

    /**
     * Get Component
     */
    override val component: LinkDeviceTutorialComponent
        get() = linkDeviceTutorialComponent

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_tutorial)
        linkDeviceTutorialComponent.inject(this)

        if(savedInstanceState == null) {

            val pageFragments = arrayOf(
                    FirstLinkTerminalPageFragment(), SecondLinkTerminalPageFragment(),
                    ThirdLinkTerminalPageFragment()

            )

            val tutorialOptions = TutorialSupportFragment
                    .newTutorialOptionsBuilder(this)
                    .setUseInfiniteScroll(false)
                    .setTutorialPageProvider(TutorialPageProvider<Fragment> { position ->
                        when (position) {
                            FIRST_PAGE_POS -> pageFragments[FIRST_PAGE_POS]
                            SECOND_PAGE_POS -> pageFragments[SECOND_PAGE_POS]
                            THIRD_PAGE_POS -> pageFragments[THIRD_PAGE_POS]
                            else -> throw IllegalArgumentException("Unknown position: $position")
                        }
                    })
                    .setOnSkipClickListener {
                        finishAndRemoveTask()
                        System.exit(0)
                    }
                    .setPagesCount(TUTORIAL_PAGES_COUNT)
                    .build()

            tutorialSupportFragment = TutorialSupportFragment
                    .newInstance(tutorialOptions)

            // Add On Tutorial Page Change Listener
            tutorialSupportFragment.addOnTutorialPageChangeListener{ position ->

                if (position >= 0 && position < pageFragments.size) {
                    if (position - 1 >= 0)
                        pageFragments[position - 1].whenPhaseIsHidden((position - 1), position)

                    if (position + 1 < pageFragments.size)
                        pageFragments[position + 1].whenPhaseIsHidden((position + 1), position)

                    Timber.d("Fragment on position %d is showed", position)
                    pageFragments[position].whenPhaseIsShowed()
                }

            }

            addFragment(R.id.fragmentContainer, tutorialSupportFragment, false)
        }
    }

    /**
     * Request Focus
     */
    override fun requestFocus() {
        var currentPos = tutorialSupportFragment.viewPager.currentItem
        if (currentPos > 0)
            tutorialSupportFragment.viewPager.currentItem = --currentPos
    }

    /**
     * Release Focus
     */
    override fun releaseFocus() {
        var currentPos = tutorialSupportFragment.viewPager.currentItem
        if (currentPos + 1 < TUTORIAL_PAGES_COUNT)
            tutorialSupportFragment.viewPager.currentItem = ++currentPos
    }

    /**
     * On Activity Results
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
            for (nestedFragment in fragment.childFragmentManager.fragments) {
                nestedFragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    /**
     * Set Current Son Entity
     */
    override fun setCurrentSonEntity(sonEntity: SonEntity?) {
        Preconditions.checkNotNull(sonEntity, "Son Entity can not be null")
        this.currentSonEntity = sonEntity
    }

    /**
     * Get Current Son Entity
     */
    override fun getCurrentSonEntity(): SonEntity? = currentSonEntity

    /**
     * Has Current Son Entity
     */
    override fun hasCurrentSonEntity(): Boolean = currentSonEntity != null

    /**
     * Go To Home
     */
    override fun goToHome() {
        navigator.showHome(this)
    }
}
