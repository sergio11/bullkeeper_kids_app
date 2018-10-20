package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.services.ILocalNotificationService
import com.sanchez.sanchez.bullkeeper_kids.services.impl.LocalNotificationServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Service Module
 */
@Module
class ServicesModule(private val context: Context)  {


    /**
     * Provide Local Notification Service
     */
    @Provides
    @Singleton
    fun provideLocalNotificationService(): ILocalNotificationService =
            LocalNotificationServiceImpl(context)

}