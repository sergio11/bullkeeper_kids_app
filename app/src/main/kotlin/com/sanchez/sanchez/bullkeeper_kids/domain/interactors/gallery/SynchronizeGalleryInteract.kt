package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.gallery

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGalleryRepository
import retrofit2.Retrofit
import javax.inject.Inject
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGalleryService
import timber.log.Timber

/**
 * Synchronize Gallery Interact
 */
class SynchronizeGalleryInteract
    @Inject constructor(
            private val context: Context,
            private val deviceGalleryService: IDeviceGalleryService,
            private val galleryRepository: IGalleryRepository,
            retrofit: Retrofit): UseCase<Int, UseCase.None>(retrofit) {


    val TAG = "SYNC_GALLERY"

    companion object {
        val BATCH_SIZE = 15
    }


    /**
     * On Execute
     */
    override suspend fun onExecuted(params: None): Int {

        val imagesList = deviceGalleryService.getImages()

        imagesList.forEach {
            if(it.id == 13998L)
                deviceGalleryService.deleteImage(it.path)
            Timber.d("Image Path -> %s", it)
        }
        return imagesList.size
    }
}