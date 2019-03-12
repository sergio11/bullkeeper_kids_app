package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial

import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.WindowManager
import com.cleveroad.slidingtutorial.TutorialPageProvider
import com.cleveroad.slidingtutorial.TutorialSupportFragment
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.AppTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.DaggerAppTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.extension.addFragment
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.legalcontent.LegalContentActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages.*
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject

/**
 * App Tutorial Activity
 */
class AppTutorialActivity : SupportActivity(), IAppTutorialHandler,
        HasComponent<AppTutorialComponent> {

    /**
     * Constants
     */
    private val FIRST_PAGE_POS = 0
    private val SECOND_PAGE_POS = 1
    private val THIRD_PAGE_POS = 2
    private val FOUR_PAGE_POS = 3
    private val FIFTH_PAGE_POS = 4
    private val SIXTH_PAGE_POS = 5
    private val SEVENTH_PAGE_POS = 6
    private val EIGHTH_PAGE_POS = 7
    private val TUTORIAL_PAGES_COUNT = 8

    /**
     * App Tutorial Component
     */
    private val appTutorialComponent: AppTutorialComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerAppTutorialComponent
                .builder()
                .applicationComponent((application as AndroidApplication).appComponent)
                .activityModule(ActivityModule(this))
                .build()
    }

    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context) =
                Intent(context, AppTutorialActivity::class.java)
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
     * Get Component
     */
    override val component: AppTutorialComponent
        get() = appTutorialComponent

    /**
     * Attach Base Context
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_tutorial)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        appTutorialComponent.inject(this)

        if(savedInstanceState == null) {

            val phaseFragments = arrayOf(
                    FirstPageFragment(), SecondPageFragment(), ThirdPageFragment(), FourPageFragment(),
                    FifthPageFragment(), SixthPageFragment(), SeventhPageFragment(), EighthPageFragment())

            val tutorialOptions = TutorialSupportFragment
                    .newTutorialOptionsBuilder(this)
                    .setUseInfiniteScroll(false)
                    .setTutorialPageProvider(TutorialPageProvider<Fragment> { position ->
                        when (position) {
                            FIRST_PAGE_POS -> phaseFragments[FIRST_PAGE_POS]
                            SECOND_PAGE_POS -> phaseFragments[SECOND_PAGE_POS]
                            THIRD_PAGE_POS -> phaseFragments[THIRD_PAGE_POS]
                            FOUR_PAGE_POS -> phaseFragments[FOUR_PAGE_POS]
                            FIFTH_PAGE_POS -> phaseFragments[FIFTH_PAGE_POS]
                            SIXTH_PAGE_POS -> phaseFragments[SIXTH_PAGE_POS]
                            SEVENTH_PAGE_POS -> phaseFragments[SEVENTH_PAGE_POS]
                            EIGHTH_PAGE_POS -> phaseFragments[EIGHTH_PAGE_POS]
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

                if (position >= 0 && position < phaseFragments.size) {
                    if (position - 1 >= 0)
                        phaseFragments[position - 1].whenPhaseIsHidden((position - 1), position)

                    if (position + 1 < phaseFragments.size)
                        phaseFragments[position + 1].whenPhaseIsHidden((position + 1), position)

                    Timber.d("Fragment on position %d is showed", position)
                    phaseFragments[position].whenPhaseIsShowed()
                }

            }

            addFragment(R.id.fragmentContainer, tutorialSupportFragment, false)
        }
    }

    /**
     * Show Legal Content
     */
    override fun showLegalContent(legalContent: LegalContentActivity.LegalTypeEnum) {
        navigator.showLegalContent(this, legalContentType = legalContent)
    }

    /**
     * On Error Ocurred
     */
    override fun onErrorOccurred(permission: String) {
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
     * Show Usage Access Settings
     */
    override fun showUsageAccessSettings() {
        navigator.showUsageAccessSettings(this)
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
     * Show Sign In
     */
    override fun showSignIn() {
        navigator.showLogin(this)
    }

    /**
     * Show Device Admin Settings
     */
    override fun showDeviceAdminSettings() {
        navigator.showEnableAdminDeviceFeatures(this)
    }

    /**
     * Show Location Source Settings
     */
    override fun showLocationSourceSettings() {
        navigator.showLocationSourceSettings(this)
    }
}
