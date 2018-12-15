package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.app.Activity
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ISmsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.ISmsRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.SmsRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.sms.SaveSmsInTheTerminalInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Sms Module
 */
@Module
class SmsModule {

    /**
     * Provide Sms Service
     */
    @Provides
    @PerActivity
    fun provideSmsService(retrofit: Retrofit) : ISmsService
            = retrofit.create(ISmsService::class.java)

    /**
     * Provide Sms Repository
     */
    @Provides
    @PerActivity
    fun provideSmsRepository(): SmsRepositoryImpl
        = SmsRepositoryImpl()

    /**
     * Provide Save Sms In The Terminal Interact
     */
    @Provides
    @PerActivity
    fun provideSaveSmsInTheTerminalInteract(
            smsService: ISmsService,
            smsRepositoryImpl: SmsRepositoryImpl,
            activity: Activity,
            preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit)
        = SaveSmsInTheTerminalInteract(activity, smsService, smsRepositoryImpl, preferenceRepository, retrofit)



}