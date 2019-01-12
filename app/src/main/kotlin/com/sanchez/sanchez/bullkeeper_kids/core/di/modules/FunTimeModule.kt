package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IFunTimeService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IFunTimeDayScheduledRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.FunTimeDayScheduledRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.funtime.SyncFunTimeInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Fun Time Module
 */
@Module
class FunTimeModule {

    /**
     * Provide Fun Time Service
     */
    @Provides
    @Singleton
    fun provideFunTimeService(retrofit: Retrofit) : IFunTimeService
            = retrofit.create(IFunTimeService::class.java)

    /**
     * Provide Fun Time Day Scheduled Repository
     */
    @Provides
    @Singleton
    fun provideFunTimeDayScheduledRepository(): IFunTimeDayScheduledRepository
        = FunTimeDayScheduledRepositoryImpl()

    /**
     * Provide Sync Fun Time Interact
     */
    @Provides
    @Singleton
    fun provideSyncFunTimeInteract(
            retrofit: Retrofit,
            funTimeService: IFunTimeService,
            funTimeDayScheduledRepository: IFunTimeDayScheduledRepository,
            preferencesRepository: IPreferenceRepository
    ): SyncFunTimeInteract
        = SyncFunTimeInteract(retrofit, funTimeService,
            funTimeDayScheduledRepository, preferencesRepository)
}