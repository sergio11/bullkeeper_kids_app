package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ChildrenModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.kidguardians.KidGuardiansActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.kidguardians.KidGuardiansActivityFragment
import dagger.Component

/**
 * Kid Guardian Component
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class],
        modules = [ActivityModule::class, ChildrenModule::class])
interface KidGuardianComponent: ActivityComponent {


    /**
     * Inject into Kid Guardians Activity
     */
    fun inject(kidGuardiansActivity: KidGuardiansActivity)

    /**
     * Inject into Kid Guardians Activity Fragment
     */
    fun inject(kidGuardiansActivityFragment: KidGuardiansActivityFragment)

}