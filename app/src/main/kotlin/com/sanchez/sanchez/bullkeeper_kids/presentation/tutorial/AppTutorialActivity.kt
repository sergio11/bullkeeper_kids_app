package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial

import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.cleveroad.slidingtutorial.TutorialPageProvider
import com.cleveroad.slidingtutorial.TutorialSupportFragment
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.extension.addFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages.FirstPageFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages.SecondPageFragment
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * App Tutorial Activity
 */
class AppTutorialActivity : AppCompatActivity() {

    /**
     * Constants
     */
    private val FIRST_PAGE_POS = 0
    private val SECOND_PAGE_POS = 1
    private val TUTORIAL_PAGES_COUNT = 2


    /**
     *
     */
    companion object {
        fun callingIntent(context: Context) =
                Intent(context, AppTutorialActivity::class.java)
    }

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

        if(savedInstanceState == null) {

            val tutorialOptions = TutorialSupportFragment
                    .newTutorialOptionsBuilder(this)
                    .setUseInfiniteScroll(false)
                    .setTutorialPageProvider(TutorialPageProvider<Fragment> { position ->
                        when (position) {
                            FIRST_PAGE_POS -> FirstPageFragment()
                            SECOND_PAGE_POS -> SecondPageFragment()
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

}
