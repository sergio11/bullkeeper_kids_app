package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ICallsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.ICallDetailRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.CallDetailRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls.AddCallDetailsFromTerminalInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls.SynchronizeTerminalCallHistoryInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Call Module
 */
@Module
class CallDetailsModule {

    /**
     * Provide Call Details Service
     */
    @Provides
    @Singleton
    fun provideCallDetailsService(retrofit: Retrofit) : ICallsService
            = retrofit.create(ICallsService::class.java)

    /**
     * Provide Call Detail Repository
     */
    @Provides
    @Singleton
    fun provideCallDetailRepository(): ICallDetailRepository
        = CallDetailRepositoryImpl()

    /**
     * Provide Save Terminal History Calls Interact
     */
    @Provides
    @Singleton
    fun provideSaveTerminalHistoryCallsInteract(
            context: Context,
            callsService: ICallsService,
            callDetailsRepository: ICallDetailRepository,
            preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit)
        = SynchronizeTerminalCallHistoryInteract(context, callsService, callDetailsRepository, preferenceRepository, retrofit)

    /**
     * Provide Add Call Details From Terminal Interact
     */
    @Provides
    @Singleton
    fun provideAddCallDetailsFromTerminalInteract(
            callsService: ICallsService,
            callDetailsRepository: ICallDetailRepository,
            preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit
    ) = AddCallDetailsFromTerminalInteract(callsService, callDetailsRepository, preferenceRepository, retrofit)

}