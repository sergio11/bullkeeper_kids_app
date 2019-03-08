package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ISmsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.ISmsRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.SmsRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.sms.SynchronizeTerminalSMSInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Sms Module
 */
@Module
class SmsModule {

    /**
     * Provide Sms Service
     */
    @Provides
    @Singleton
    fun provideSmsService(retrofit: Retrofit) : ISmsService
            = retrofit.create(ISmsService::class.java)

    /**
     * Provide Sms Repository
     */
    @Provides
    @Singleton
    fun provideSmsRepository(): ISmsRepository
        = SmsRepositoryImpl()

    /**
     * Provide Save Sms In The Terminal Interact
     */
    @Provides
    @Singleton
    fun provideSaveSmsInTheTerminalInteract(
            smsService: ISmsService,
            smsRepository: ISmsRepository,
            context: Context,
            preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit)
        = SynchronizeTerminalSMSInteract(context, smsService, smsRepository, preferenceRepository, retrofit)

}