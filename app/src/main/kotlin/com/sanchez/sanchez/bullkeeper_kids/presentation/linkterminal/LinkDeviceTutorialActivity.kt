package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal


import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.WindowManager
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
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.GuardianModule
import com.sanchez.sanchez.bullkeeper_kids.core.extension.addFragment
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportActivity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.KidEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages.*
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
    private val FOUR_PAGE_POS = 3
    private val FIFTH_PAGE_POS = 4
    private val TUTORIAL_PAGES_COUNT = 5

    /**
     * App Tutorial Component
     */
    private val linkDeviceTutorialComponent: LinkDeviceTutorialComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerLinkDeviceTutorialComponent
                .builder()
                .applicationComponent((application as AndroidApplication).appComponent)
                .activityModule(ActivityModule(this))
                .linkTerminalModule(LinkTerminalModule())
                .guardianModule(GuardianModule())
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
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * Tutorial Support Fragment
     */
    private lateinit var tutorialSupportFragment: TutorialSupportFragment

    /**
     * Current Son Entity
     */
    private var currentKidEntity: KidEntity? = null

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
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        linkDeviceTutorialComponent.inject(this)

        if (savedInstanceState == null)
            buildTutorial()
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
    override fun setCurrentKidEntity(kidEntity: KidEntity?) {
        Preconditions.checkNotNull(kidEntity, "Son Entity can not be null")
        this.currentKidEntity = kidEntity
    }

    /**
     * Get Current Son Entity
     */
    override fun getCurrentKidEntity(): KidEntity? = currentKidEntity

    /**
     * Has Current Son Entity
     */
    override fun hasCurrentKidEntity(): Boolean = currentKidEntity != null

    /**
     * Go To Home
     */
    override fun goToHome() {
        navigator.showHome(this)
    }

    /**
     * Build Tutorial
     */
    private fun buildTutorial(){

        val pageFragments = arrayOf(
                FirstLinkTerminalPageFragment(), SecondLinkTerminalPageFragment(),
                ThirdLinkTerminalPageFragment(), FourLinkTerminalPageFragment(),
                FifthLinkTerminalPageFragment()
        )

        val tutorialOptions = TutorialSupportFragment
                .newTutorialOptionsBuilder(this)
                .setUseInfiniteScroll(false)
                .setTutorialPageProvider(TutorialPageProvider<Fragment> { position ->
                    when (position) {
                        FIRST_PAGE_POS -> pageFragments[FIRST_PAGE_POS]
                        SECOND_PAGE_POS -> pageFragments[SECOND_PAGE_POS]
                        THIRD_PAGE_POS -> pageFragments[THIRD_PAGE_POS]
                        FOUR_PAGE_POS -> pageFragments[FOUR_PAGE_POS]
                        FIFTH_PAGE_POS -> pageFragments[FIFTH_PAGE_POS]
                        else -> throw IllegalArgumentException("Unknown position: $position")
                    }
                })
                .setOnSkipClickListener {
                    goToLogin()
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

    /**
     * Go To Login
     */
    override fun goToLogin() {
        navigator.showLogin(this)
    }

}
