package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IScheduledBlocksService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.ScheduledBlocksRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.scheduledblocks.SynchronizeScheduledBlocksInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Scheduled Blocks Module
 */
@Module
class ScheduledBlocksModule {

    /**
     * Provide Scheduled Blocks Service
     */
    @Provides
    @Singleton
    fun provideScheduledBlocksService(retrofit: Retrofit) : IScheduledBlocksService
            = retrofit.create(IScheduledBlocksService::class.java)

    /**
     * Provide Scheduled Blocks Repository
     */
    @Provides
    @Singleton
    fun provideScheduledBlocksRepository(): ScheduledBlocksRepositoryImpl
        = ScheduledBlocksRepositoryImpl()

    /**
     * Provide Synchronize Scheduled Blocks Interact
     */
    @Provides
    @Singleton
    fun provideSynchronizeScheduledBlocksInteract(
            appContext: Context,
            retrofit: Retrofit,
            scheduledBlocksService: IScheduledBlocksService,
            scheduledBlocksRepository: ScheduledBlocksRepositoryImpl,
            preferenceRepository: IPreferenceRepository)
        = SynchronizeScheduledBlocksInteract(appContext, retrofit, scheduledBlocksService,
            scheduledBlocksRepository, preferenceRepository)

}