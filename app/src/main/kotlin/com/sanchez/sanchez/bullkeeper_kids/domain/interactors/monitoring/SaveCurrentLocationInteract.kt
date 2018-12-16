package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.monitoring

import android.content.Context
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveCurrentLocation
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.CurrentLocationDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ILocationService
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Save Current Location Interact
 */
class SaveCurrentLocationInteract
    @Inject constructor(retrofit: Retrofit,
                        private val preferenceRepository: IPreferenceRepository,
                        private val locationService: ILocationService)
        : UseCase<CurrentLocationDTO, SaveCurrentLocationInteract.Params>(retrofit) {

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): CurrentLocationDTO {
        Preconditions.checkNotNull(params, "Params can not be null")
        Preconditions.checkNotNull(params.latitude, "Latitude can not be null")
        Preconditions.checkNotNull(params.longitude, "Longitude can not be null")
        val kid = preferenceRepository.getPrefKidIdentity()
        val response = locationService.saveCurrentLocation(kid,
                SaveCurrentLocation(params.latitude, params.longitude)).await()

        return response.data!!
    }


    /**
     * Params
     */
    data class Params(
            var latitude: Double,
            var longitude: Double
    )

}