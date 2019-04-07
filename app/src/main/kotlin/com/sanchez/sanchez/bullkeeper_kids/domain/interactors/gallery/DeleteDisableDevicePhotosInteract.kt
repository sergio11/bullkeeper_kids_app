package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.gallery

import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IDevicePhotosService
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGalleryService
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Delete Disable Device Photos Interact
 */
class DeleteDisableDevicePhotosInteract
    @Inject constructor(
            private val devicePhotosService: IDevicePhotosService,
            private val deviceGalleryService: IDeviceGalleryService,
            private val preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit): UseCase<Unit, DeleteDisableDevicePhotosInteract.Params>(retrofit) {
    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params) {

        if(!params.imageFileList.isNullOrEmpty()) {
            deviceGalleryService.deleteImage(params.imageFileList)
        } else {

            if(preferenceRepository.getPrefKidIdentity()
                    != IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE &&
                    preferenceRepository.getPrefTerminalIdentity()
                        != IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE) {

                val response = devicePhotosService.getListOfDisabledDevicePhotos(
                        preferenceRepository.getPrefKidIdentity(),
                        preferenceRepository.getPrefTerminalIdentity()).await()

                response.httpStatus?.let {
                    if (it == "OK") {
                        response.data?.filter { !it.path.isEmpty() }
                                ?.map { it.path }?.let {
                                    deviceGalleryService.deleteImage(it)
                                }
                    }
                }

            }
        }
    }

    /**
     * Params
     */
    data class Params(
            val imageFileList: List<String>? = null
    )
}