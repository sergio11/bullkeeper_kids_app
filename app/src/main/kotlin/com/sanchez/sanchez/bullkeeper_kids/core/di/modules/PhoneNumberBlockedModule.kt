package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IPhoneNumberService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPhoneNumberRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.PhoneNumberRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.phonenumber.GetBlockedPhoneNumbersInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Phone Number Blocked Module
 */
@Module
class PhoneNumberBlockedModule {

    /**
     * Provide Phone Number Service
     */
    @Provides
    @Singleton
    fun providePhoneNumberService(retrofit: Retrofit): IPhoneNumberService {
        return retrofit.create(IPhoneNumberService::class.java)
    }

    /**
     * Provide Phone Number Repository
     */
    @Provides
    @Singleton
    fun providePhoneNumberRepository(): IPhoneNumberRepository
        = PhoneNumberRepositoryImpl()

    /**
     * Provide Get Blocked Phone Numbers Interact
     */
    @Provides
    @Singleton
    fun provideGetBlockedPhoneNumbersInteract(
            retrofit: Retrofit,
            preferenceRepository: IPreferenceRepository,
            phoneNumberService: IPhoneNumberService,
            phoneNumberRepository: IPhoneNumberRepository): GetBlockedPhoneNumbersInteract {
        return GetBlockedPhoneNumbersInteract(retrofit, preferenceRepository,
                phoneNumberService, phoneNumberRepository)
    }

}