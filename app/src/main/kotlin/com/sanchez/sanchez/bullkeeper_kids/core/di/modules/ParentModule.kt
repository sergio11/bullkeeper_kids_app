package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IGuardiansService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.children.GetSelfChildrenInteract
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Parent Module
 */
@Module
class ParentModule(private val application: AndroidApplication) {

    /**
     * Provide Parent Service
     */
    @Provides
    @PerActivity
    fun provideParentService(retrofit: Retrofit): IGuardiansService
            = retrofit.create(IGuardiansService::class.java)

    /**
     * Provide Get Self Children Interact
     */
    @Provides
    @PerActivity
    fun provideGetSelfChildrenInteract(retrofit: Retrofit,
                                       parentService: IGuardiansService,
                                       appHelper: ApiEndPointsHelper): GetSelfChildrenInteract
        = GetSelfChildrenInteract(retrofit, parentService, appHelper)

}