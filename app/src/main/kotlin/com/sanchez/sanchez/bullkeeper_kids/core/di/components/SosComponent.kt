package com.sanchez.sanchez.bullkeeper_kids.core.di.components


import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.KidRequestModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.SosModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.sos.SosActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.sos.SosActivityFragment
import dagger.Component

/**
 * Sos Component
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class],
        modules = [ActivityModule::class, KidRequestModule::class, SosModule::class])
interface SosComponent: ActivityComponent {

    /**
     * Inject into Sos Activity
     */
    fun inject(sosActivity: SosActivity)

    /**
     * Inject into Sos Activity Fragment
     */
    fun inject(sosActivityFragment: SosActivityFragment)
}