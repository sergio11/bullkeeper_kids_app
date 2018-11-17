package com.sanchez.sanchez.bullkeeper_kids.core.di.viewmodel

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

/**
 * View Model Module
 */
@Module
abstract class ViewModelModule {

    /**
     * Bind View Model Factory
     */
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


}