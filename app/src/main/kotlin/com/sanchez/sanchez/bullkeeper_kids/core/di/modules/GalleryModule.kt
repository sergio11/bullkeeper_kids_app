package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import android.os.Handler
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGalleryRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.GalleryRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.gallery.SynchronizeGalleryInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.observers.MediaStoreImagesContentObserver
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGalleryService
import com.sanchez.sanchez.bullkeeper_kids.services.impl.DeviceGalleryServiceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Gallery Module
 */
@Module
class GalleryModule {

    /**
     * Provide Device Gallery Service
     */
    @Provides
    @Singleton
    fun provideDeviceGalleryService(context: Context): IDeviceGalleryService
            = DeviceGalleryServiceImpl(context)

    /**
     * Provide Synchronize Gallery Interact
     */
    @Provides
    @Singleton
    fun provideSynchronizeGalleryInteract(context: Context,
                                          deviceGalleryService: IDeviceGalleryService,
                                          galleryRepository: IGalleryRepository,
                                          retrofit: Retrofit): SynchronizeGalleryInteract
        = SynchronizeGalleryInteract(context, deviceGalleryService, galleryRepository, retrofit)


    /**
     * Provide Gallery Repository
     */
    @Provides
    @Singleton
    fun provideGalleryRepository(): IGalleryRepository
        = GalleryRepositoryImpl()

    /**
     * Provide Media Storage Images Content Observer
     */
    @Provides
    @Singleton
    fun provideMediaStoreImagesContentObserver(
            handler: Handler,
            synchronizeGalleryInteract: SynchronizeGalleryInteract
            ): MediaStoreImagesContentObserver
        = MediaStoreImagesContentObserver(handler, synchronizeGalleryInteract)
}