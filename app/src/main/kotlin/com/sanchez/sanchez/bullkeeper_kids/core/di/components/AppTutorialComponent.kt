package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.AppTutorialActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages.*
import dagger.Component

/**
 * App Tutorial Component
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class],
        modules = [ActivityModule::class])
interface AppTutorialComponent: ActivityComponent {

    /**
     * Inject into App Tutorial Activity
     */
    fun inject(appTutorialActivity: AppTutorialActivity)

    /**
     * Inject into First Page Fragment
     */
    fun inject(firstPageFragment: FirstPageFragment)

    /**
     * Inject into Second Page Fragment
     */
    fun inject(secondPageFragment: SecondPageFragment)

    /**
     * Inject into Third Page Fragment
     */
    fun inject(thirdPageFragment: ThirdPageFragment)

    /**
     * Inject into Four Page Fragments
     */
    fun inject(fourPageFragment: FourPageFragment)

    /**
     * Inject into Fifth Page Fragment
     */
    fun inject(fifthPageFragment: FifthPageFragment)

    /**
     * Inject into Sixth Page Fragment
     */
    fun inject(sixthPageFragment: SixthPageFragment)

    /**
     * Inject into Seventh Page Fragment
     */
    fun inject(seventhPageFragment: SeventhPageFragment)


}