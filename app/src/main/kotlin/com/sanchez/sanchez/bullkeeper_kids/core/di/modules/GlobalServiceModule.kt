package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.impl.SoundPoolManagerImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.location.GetAddressFromCurrentLocationInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.impl.PreferenceRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.utils.IAuthTokenAware
import com.sanchez.sanchez.bullkeeper_kids.services.IGeolocationService
import com.sanchez.sanchez.bullkeeper_kids.services.ILocalNotificationService
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import com.sanchez.sanchez.bullkeeper_kids.services.impl.GeolocationServiceImpl
import com.sanchez.sanchez.bullkeeper_kids.services.impl.LocalNotificationServiceImpl
import com.sanchez.sanchez.bullkeeper_kids.services.impl.UsageStatsServiceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Service Module
 */
@Module
class GlobalServiceModule(private val context: Context)  {


    /**
     * Provide Local Notification Service
     */
    @Provides
    @Singleton
    fun provideLocalNotificationService(): ILocalNotificationService =
            LocalNotificationServiceImpl(context)


    /**
     * Provide Usage Stats Service
     */
    @Provides
    @Singleton
    fun provideUsageStatsService(context: Context): IUsageStatsService =
            UsageStatsServiceImpl(context)

    /**
     * Provide Preference Repository
     */
    @Provides
    @Singleton
    fun providePreferenceRepository(context: Context): IPreferenceRepository =
            PreferenceRepositoryImpl(context)


    /**
     * Provide Auth Token Aware
     * @param preferencesRepositoryImpl
     * @return
     */
    @Provides
    @Singleton
    fun provideAuthTokenAware(preferenceRepository: IPreferenceRepository): IAuthTokenAware
            = preferenceRepository

    /**
     * Provide Sound Manager
     */
    @Provides
    @Singleton
    fun provideSoundManager(context: Context): ISoundManager =
            SoundPoolManagerImpl(context)

    /**
     * Provide Get Address From Current Location Interact
     */
    @Provides
    @Singleton
    fun provideGetAddressFromCurrentLocationInteract(
            context: Context,
            preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit
    ): GetAddressFromCurrentLocationInteract =
            GetAddressFromCurrentLocationInteract(context, preferenceRepository, retrofit)

    /**
     * Provide GeoLocation Service
     */
    @Provides
    @Singleton
    fun provideGeolocationService(context: Context): IGeolocationService
        = GeolocationServiceImpl(context)


}