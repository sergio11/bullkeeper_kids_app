package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.gallery

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.extension.empty
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.GalleryImageEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddDevicePhotoDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.DevicePhotoDTO
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
            private val objectMapper: ObjectMapper,
            retrofit: Retrofit): UseCase<Int, UseCase.None>(retrofit) {


    private val MAX_IMAGES_TO_UPLOAD = 15
    private val DEVICE_PHOTO_INFO_KEY = "devicePhoto"
    private val DEVICE_PHOTO_IMAGE_KEY = "devicePhotoImage"

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

        return photosToUpload.shuffled().take(MAX_IMAGES_TO_UPLOAD).map { AddDevicePhotoDTO(
                displayName = it.displayName,
                path = it.path,
                dateAdded = it.dateAdded,
                dateModified = it.dateModified,
                dateTaken = it.dateTaken,
                height = it.height,
                width = it.width,
                orientation = it.orientation,
                size = it.size,
                localId = it.id.toString(),
                kid = preferenceRepository.getPrefKidIdentity(),
                terminal = preferenceRepository.getPrefTerminalIdentity()
        ) }.filter {
            !it.kid.isNullOrEmpty() && !it.terminal.isNullOrEmpty()
        }.filter {
            val galleryImageFile = File(it.path)
            galleryImageFile.exists() && galleryImageFile.canRead()
        }.mapNotNull { devicePhoto ->

            var response: APIResponse<DevicePhotoDTO>? = null

            try {

                response = devicePhotosService
                        .saveDevicePhoto(devicePhoto.kid!!, devicePhoto.terminal!!, MultipartBody.Builder().apply {
                            setType(MultipartBody.FORM)
                            addFormDataPart(DEVICE_PHOTO_INFO_KEY, devicePhoto.displayName, RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                                    objectMapper.writeValueAsString(devicePhoto)))
                            addFormDataPart(DEVICE_PHOTO_IMAGE_KEY, devicePhoto.displayName
                                    ?: String.empty(),
                                    RequestBody.create(MediaType.parse("multipart/form-data"), File(devicePhoto.path)))
                        }.build())
                        .await()

            } catch(ex: Exception){
                Timber.d("Fail save gallery image")
            }

            response

        }.filter { it.httpStatus == "OK" }.mapNotNull { it.data }.mapNotNull {devicePhotoDTO ->
            galleryRepository.findById(devicePhotoDTO.localId)?.also {
                it.serverId = devicePhotoDTO.identity
                it.sync = 1
                it.remove = 0
                galleryRepository.save(it)
                Timber.d("%s - Gallery Image %s was sync successfully", TAG, it.id?.toString())
            }
        }.size

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