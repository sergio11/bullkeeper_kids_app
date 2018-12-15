package com.sanchez.sanchez.bullkeeper_kids.core.di.modules


import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ICallsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.CallDetailRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls.SaveTerminalHistoryCallsInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Call Module
 */
@Module
class CallDetailsModule {

    /**
     * Provide Call Details Service
     */
    @Provides
    @PerActivity
    fun provideCallDetailsService(retrofit: Retrofit) : ICallsService
            = retrofit.create(ICallsService::class.java)

    /**
     * Provide Call Detail Repository
     */
    @Provides
    @PerActivity
    fun provideCallDetailRepository(): CallDetailRepositoryImpl
        = CallDetailRepositoryImpl()

    /**
     * Provide Save Terminal History Calls Interact
     */
    @Provides
    @PerActivity
    fun provideSaveTerminalHistoryCallsInteract(
            context: Context,
            callsService: ICallsService,
            callDetailsRepositoryImpl: CallDetailRepositoryImpl,
            preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit)
        = SaveTerminalHistoryCallsInteract(context, callsService, callDetailsRepositoryImpl, preferenceRepository, retrofit)

}