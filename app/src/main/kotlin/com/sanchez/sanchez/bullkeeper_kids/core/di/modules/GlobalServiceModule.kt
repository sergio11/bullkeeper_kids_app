package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.impl.PreferenceRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.utils.IAuthTokenAware
import com.sanchez.sanchez.bullkeeper_kids.services.IAuthenticatorService
import com.sanchez.sanchez.bullkeeper_kids.services.ILocalNotificationService
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import com.sanchez.sanchez.bullkeeper_kids.services.impl.AuthenticatorServiceImpl
import com.sanchez.sanchez.bullkeeper_kids.services.impl.LocalNotificationServiceImpl
import com.sanchez.sanchez.bullkeeper_kids.services.impl.UsageStatsServiceImpl
import dagger.Module
import dagger.Provides
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

}