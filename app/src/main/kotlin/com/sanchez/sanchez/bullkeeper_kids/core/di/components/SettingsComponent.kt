package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.SettingsModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.settings.SettingsActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.settings.SettingsActivityFragment
import dagger.Component

/**
 * Settings Component
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class],
        modules = [ActivityModule::class, SettingsModule::class])
interface SettingsComponent: ActivityComponent {

    /**
     * Inject into Settings Activity Fragment
     */
    fun inject(settingsActivityFragment: SettingsActivityFragment)

    /**
     * Inject into Settings Activity
     */
    fun inject(settingsActivity: SettingsActivity)
}