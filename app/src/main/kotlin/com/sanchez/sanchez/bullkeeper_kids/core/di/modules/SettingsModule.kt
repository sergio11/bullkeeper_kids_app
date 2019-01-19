package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.settings.SettingsViewModel
import dagger.Module
import dagger.Provides

/**
 * Settings Module
 */
@Module
class SettingsModule {

    /**
     * Provide Settings View Model
     */
    @Provides
    @PerActivity
    fun provideSettingsViewModel(): SettingsViewModel
        = SettingsViewModel()
}