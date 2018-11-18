package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ITerminalService
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.SaveTerminalInteract
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Terminal Module
 */
@Module
class TerminalModule(private val application: AndroidApplication) {


    /**
     * Provide Terminal Service
     */
    @Provides
    @PerActivity
    fun provideTerminalService(retrofit: Retrofit): ITerminalService
        = retrofit.create(ITerminalService::class.java)

    /**
     * Provide Save Terminal Interact
     */
    @Provides
    @PerActivity
    fun provideSaveTerminalInteract(retrofit: Retrofit, terminalService: ITerminalService)
        = SaveTerminalInteract(retrofit, terminalService)

}