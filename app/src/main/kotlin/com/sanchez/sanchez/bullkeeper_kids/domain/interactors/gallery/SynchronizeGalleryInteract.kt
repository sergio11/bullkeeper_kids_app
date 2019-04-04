package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.gallery

import android.content.Context
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.extension.empty
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.GalleryImageEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddDevicePhotoDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IDevicePhotosService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGalleryRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGalleryService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File

/**
 * Synchronize Gallery Interact
 */
class SynchronizeGalleryInteract
    @Inject constructor(
            private val context: Context,
            private val deviceGalleryService: IDeviceGalleryService,
            private val galleryRepository: IGalleryRepository,
            private val devicePhotosService: IDevicePhotosService,
            private val preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit): UseCase<Int, UseCase.None>(retrofit) {


    val TAG = "SYNC_GALLERY"

    /**
     * Get Photos To Save
     * @param photosInGallery
     * @param photosSaved
     */
    private fun getPhotosToSave(
            photosInGallery: List<GalleryImageEntity>,
            photosSaved: List<GalleryImageEntity>): List<GalleryImageEntity> {

        val photosToSave = arrayListOf<GalleryImageEntity>()
        for(photoInGallery in photosInGallery) {
            var isFound = false
            for(photoSaved in photosSaved) {
                if(photoInGallery.id == photoSaved.id) {
                    isFound = true
                    if(photoSaved.sync == 0 || photoSaved.remove == 1 )
                        photosToSave.add(photoSaved)
                }
            }
            if(!isFound)
                photosToSave.add(photoInGallery)
        }
        return photosToSave
    }

    /**
     * Get Photos To Remove
     */
    private fun getPhotosToRemove(
            photosInGallery: List<GalleryImageEntity>,
            photosSaved: List<GalleryImageEntity>
    ): List<GalleryImageEntity> {

        val photosToRemove = arrayListOf<GalleryImageEntity>()
        for(photoSaved in photosSaved) {
            if(photoSaved.remove == 1) {
                photosToRemove.add(photoSaved)
                continue
            }

            var isFound = false
            for(photoInGallery in photosInGallery){
                if(photoSaved.id == photoInGallery.id)
                    isFound = true
            }

            if(!isFound) {
                if(photoSaved.sync == 1 && photoSaved.serverId != null) {
                    photoSaved.remove = 1
                    photosToRemove.add(photoSaved)
                } else {
                    galleryRepository.delete(photoSaved)
                }
            }
        }

        return photosToRemove
    }

    /**
     * Get Photos To Synchronize
     */
    private fun getPhotosToSynchronize(): Pair<List<GalleryImageEntity>, List<GalleryImageEntity>> {

        // Photos To Save
        val photosToSave = arrayListOf<GalleryImageEntity>()
        // Photos To Remove
        val photosToRemove = arrayListOf<GalleryImageEntity>()
        // Photos In Gallery
        val photosInGallery = deviceGalleryService.getImages()
        // Photos Saved
        val photosSaved = galleryRepository.list()

        if(photosInGallery.isEmpty() && photosSaved.isNotEmpty()) {

            // Delete sync photos
            photosToRemove.addAll(photosSaved
                    .filter { it.sync == 1 && it.serverId != null }
                    .onEach { it.remove = 1 }
                    .map { it })

            // delete unsync photos
            galleryRepository.delete(
                    photosSaved
                            .filter { it.sync == 0 }
                            .map { it }
            )

        } else if(photosInGallery.isNotEmpty() && photosSaved.isEmpty()) {

            // save all photo in gallery
            photosToSave.addAll(photosInGallery)

        } else if(photosInGallery.isNotEmpty() && photosSaved.isNotEmpty()) {

            // Get Photos to Save
            photosToSave.addAll(getPhotosToSave(photosInGallery, photosSaved))
            // Get Photos To Remove
            photosToRemove.addAll(getPhotosToRemove(photosInGallery, photosSaved))

        }

        return Pair(photosToSave, photosToRemove)
    }

    /**
     * Upload Device Photos
     */
    private suspend fun uploadDevicePhoto(photosToUpload: List<GalleryImageEntity>): Int {
        Preconditions.checkNotNull(photosToUpload, "Photos To Upload can not be null")
        Preconditions.checkState(!photosToUpload.isEmpty(), "Photos To Upload can not be empty")

        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()

        val devicePhotosUploaded = arrayListOf<GalleryImageEntity>()

        photosToUpload.forEach { galleryImageEntity ->

            val galleryImageFile = File(galleryImageEntity.path)

            if(galleryImageFile.exists() && galleryImageFile.canRead()) {

                val requestBody = MultipartBody.Builder().apply {
                    setType(MultipartBody.FORM)
                    addFormDataPart("display_name", galleryImageEntity.displayName ?: String.empty())
                    addFormDataPart("path", galleryImageEntity.path ?: String.empty())
                    addFormDataPart("date_added", galleryImageEntity.dateAdded?.toString() ?: String.empty())
                    addFormDataPart("date_modified", galleryImageEntity.dateModified?.toString() ?: String.empty())
                    addFormDataPart("date_taken", galleryImageEntity.dateTaken?.toString() ?: String.empty())
                    addFormDataPart("height", galleryImageEntity.height?.toString() ?: String.empty())
                    addFormDataPart("width", galleryImageEntity.width?.toString() ?: String.empty())
                    addFormDataPart("orientation", galleryImageEntity.orientation?.toString() ?: String.empty())
                    addFormDataPart("size", galleryImageEntity.size?.toString() ?: String.empty())
                    addFormDataPart("local_id", galleryImageEntity.id?.toString() ?: String.empty())
                    addFormDataPart("kid", kid)
                    addFormDataPart("terminal", terminal)
                    addFormDataPart("image", galleryImageEntity.displayName ?: String.empty(),
                            RequestBody.create(MediaType.parse("multipart/form-data"), galleryImageFile))
                }.build()

                val response = devicePhotosService
                        .saveDevicePhoto(kid, terminal, requestBody)
                        .await()

                response.httpStatus?.let {
                    if(it == "OK") {
                        response.data?.let {devicePhotoDTO ->
                            if(galleryImageEntity.id.toString() == devicePhotoDTO.localId) {
                                galleryImageEntity.serverId = devicePhotoDTO.identity
                                galleryImageEntity.sync = 1
                                galleryImageEntity.remove = 0
                            }
                        }
                        // Save Sync Photos
                        galleryRepository.save(galleryImageEntity)
                        // Add To List
                        devicePhotosUploaded.add(galleryImageEntity)
                    } else {
                        Timber.d("No Success Sync Photos")
                    }

                }

            }

        }

        return devicePhotosUploaded.size

    }

    /**
     * Delete Device Photos
     */
    private suspend fun deleteDevicePhotos(photosToRemove: List<GalleryImageEntity>): Int {
        Preconditions.checkNotNull(photosToRemove, "Photos To Remove can not be null")
        Preconditions.checkState(!photosToRemove.isEmpty(), "Photos To remove can not be empty")

        val kidId = preferenceRepository.getPrefKidIdentity()
        val terminalId = preferenceRepository.getPrefTerminalIdentity()

        var totalDevicePhotosDeleted  = 0

        val response = devicePhotosService.deleteDevicePhotos(
                kidId, terminalId, photosToRemove.filter { it.sync == 1 && it.serverId != null }
                .map { it.serverId!! }
        ).await()

        response.httpStatus?.let {

            if (it == "OK") {
                totalDevicePhotosDeleted = photosToRemove.size
                // save all as removed = true
                photosToRemove.onEach { it.remove = 1 }
                galleryRepository.save(photosToRemove)
                galleryRepository.delete(photosToRemove)
            }

        }

        return totalDevicePhotosDeleted

    }

    /**
     * On Execute
     */
    override suspend fun onExecuted(params: None): Int {

        val photosToSync = getPhotosToSynchronize()
        val photosToUpload = photosToSync.first
        val photosToRemove = photosToSync.second

        var totalPhotosSync = 0

        // Photos To Upload
        if(photosToUpload.isNotEmpty()) {
            // Save Photo
            galleryRepository.save(photosToUpload)
            Timber.d("%s Photos To Sync -> %s", TAG, photosToUpload.size)
            // Upload Photo Details
            totalPhotosSync += uploadDevicePhoto(photosToUpload)
        }

        // Remove Photos
        if(photosToRemove.isNotEmpty()) {
            galleryRepository.save(photosToRemove)
            Timber.d("%s Photos To Remove -> %s", TAG, photosToRemove.size)
            totalPhotosSync += deleteDevicePhotos(photosToRemove)
        }

        return totalPhotosSync

    }
}