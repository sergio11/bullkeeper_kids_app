package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IChildrenService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.children.GetConfirmedGuardiansForKidInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Children Module
 */
@Module
class ChildrenModule {

    /**
     * Provide Children Service
     *
     * @param retrofit
     */
    @Provides
    @PerActivity
    fun provideChildrenService(retrofit: Retrofit): IChildrenService
            = retrofit.create(IChildrenService::class.java)


    /**
     * Provide Get Confirmed Guardians For Kid Interact
     *
     * @param appContext
     * @param childrenService
     * @param preferenceRepository
     * @param retrofit
     */
    @Provides
    @PerActivity
    fun provideGetConfirmedGuardiansForKidInteract(
            appContext: Context,
            childrenService: IChildrenService,
            preferenceRepository: IPreferenceRepository,
            apiEndPointsHelper: ApiEndPointsHelper,
            retrofit: Retrofit
    ): GetConfirmedGuardiansForKidInteract
        = GetConfirmedGuardiansForKidInteract(appContext, childrenService,
            preferenceRepository, apiEndPointsHelper, retrofit)
}