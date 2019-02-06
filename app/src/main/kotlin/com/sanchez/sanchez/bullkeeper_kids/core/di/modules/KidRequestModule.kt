package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IKidRequestService
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.kidrequest.SendRequestInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Kid Request Module
 */
@Module
class KidRequestModule {

    /**
     * Provide Kid Request Service
     */
    @Provides
    @PerActivity
    fun provideKidRequestService(retrofit: Retrofit) : IKidRequestService
            = retrofit.create(IKidRequestService::class.java)


    /**
     * Provide Send Request Interact
     */
    @Provides
    @PerActivity
    fun provideSendRequestInteract(
            appContext: Context,
            kidRequestService: IKidRequestService,
            preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit
    ): SendRequestInteract
        = SendRequestInteract(appContext, kidRequestService, preferenceRepository, retrofit)
}