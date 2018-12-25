package com.sanchez.sanchez.bullkeeper_kids.core.di.components


import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.PickMeUpModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.pickmeup.PickMeUpActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.pickmeup.PickMeUpActivityFragment
import dagger.Component

/**
 * Pick Me Up Component
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class],
        modules = [ActivityModule::class, PickMeUpModule::class])
interface PickMeUpComponent: ActivityComponent {

    /**
     * Inject into Pick Me Up Activity
     */
    fun inject(pickMeUpActivity: PickMeUpActivity)

    /**
     * Inject into Pick Me Up Activity Fragment
     */
    fun inject(pickMeUpActivityFragment: PickMeUpActivityFragment)
}