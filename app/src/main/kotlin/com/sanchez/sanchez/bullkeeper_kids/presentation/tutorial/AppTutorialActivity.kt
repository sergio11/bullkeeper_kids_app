package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial

import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.cleveroad.slidingtutorial.TutorialPageProvider
import com.cleveroad.slidingtutorial.TutorialSupportFragment
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.extension.addFragment
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.Navigator
import com.sanchez.sanchez.bullkeeper_kids.presentation.legalcontent.LegalContentActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages.FirstPageFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages.SecondPageFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages.ThirdPageFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages.FourPageFragment
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject

/**
 * App Tutorial Activity
 */
class AppTutorialActivity : AppCompatActivity(), IAppTutorialHandler {


    /**
     * Constants
     */
    private val FIRST_PAGE_POS = 0
    private val SECOND_PAGE_POS = 1
    private val THIRD_PAGE_POS = 2
    private val FOUR_PAGE_POS = 3
    private val TUTORIAL_PAGES_COUNT = 4

    /**
     * App Component
     */
    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }


    /**
     *
     */
    companion object {
        fun callingIntent(context: Context) =
                Intent(context, AppTutorialActivity::class.java)
    }

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: Navigator

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
        appComponent.inject(this)

        if(savedInstanceState == null) {

            val tutorialOptions = TutorialSupportFragment
                    .newTutorialOptionsBuilder(this)
                    .setUseInfiniteScroll(false)
                    .setTutorialPageProvider(TutorialPageProvider<Fragment> { position ->
                        when (position) {
                            FIRST_PAGE_POS -> FirstPageFragment()
                            SECOND_PAGE_POS -> SecondPageFragment()
                            THIRD_PAGE_POS -> ThirdPageFragment()
                            FOUR_PAGE_POS -> FourPageFragment()
                            else -> throw IllegalArgumentException("Unknown position: $position")
                        }
                    })
                    .setOnSkipClickListener {
                        finishAndRemoveTask()
                        System.exit(0)
                    }
                    .setPagesCount(TUTORIAL_PAGES_COUNT)
                    .build()

            val tutorialFragment = TutorialSupportFragment
                    .newInstance(tutorialOptions)

            addFragment(R.id.fragmentContainer, tutorialFragment, false)
        }
    }

    /**
     * Show Legal Content
     */
    override fun showLegalContent(legalContent: LegalContentActivity.LegalTypeEnum) {
        navigator.showLegalContent(context = applicationContext, legalContentType = legalContent)
    }

}
