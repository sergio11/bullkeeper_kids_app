package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IHeartBeatService
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ILocationService
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.monitoring.NotifyHeartBeatInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.monitoring.SaveCurrentLocationInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Monitoring Module
 */
@Module
class MonitoringModule {

    /**
     * Provide HeartBeat Service
     */
    @Provides
    @Singleton
    fun provideHeartBeatService(retrofit: Retrofit) : IHeartBeatService
            = retrofit.create(IHeartBeatService::class.java)

    /**
     * Provide Notify Heart Beat Interact
     */
    @Provides
    @Singleton
    fun provideNotifyHeartBeatInteract(
            retrofit: Retrofit,
            context: Context,
            preferenceRepository: IPreferenceRepository,
            heartBeatService: IHeartBeatService): NotifyHeartBeatInteract
        = NotifyHeartBeatInteract(retrofit, context, preferenceRepository, heartBeatService)

    /**
     * Provide Location Service
     */
    @Provides
    @Singleton
    fun provideLocationService(retrofit: Retrofit): ILocationService
        = retrofit.create(ILocationService::class.java)


    /**
     * Provide Save Current Location Interact
     */
    @Provides
    @Singleton
    fun provideSaveCurrentLocationInteract(
            retrofit: Retrofit,
            preferenceRepository: IPreferenceRepository,
            locationService: ILocationService
    ): SaveCurrentLocationInteract
        = SaveCurrentLocationInteract(retrofit, preferenceRepository, locationService)

}