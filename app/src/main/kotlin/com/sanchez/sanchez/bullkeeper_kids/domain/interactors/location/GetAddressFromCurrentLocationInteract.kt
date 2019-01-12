package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.location


import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject
import android.location.Geocoder
import java.util.*
import com.sanchez.sanchez.bullkeeper_kids.core.extension.empty
import timber.log.Timber
import java.io.IOException
import java.lang.Exception


/**
 * Get Address From Current Location
 */
class GetAddressFromCurrentLocationInteract
    @Inject constructor(
            private val context: Context,
            private val preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit) : UseCase<String, UseCase.None>(retrofit) {

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None): String {

        var addressResult: String = String.empty()

        var lat: Double? = null
        var log: Double? = null

        try {

            if (!preferenceRepository.getCurrentLatitude().isEmpty())
                lat = preferenceRepository.getCurrentLatitude().toDouble()

            if (!preferenceRepository.getCurrentLongitude().isEmpty())
                log = preferenceRepository.getCurrentLongitude().toDouble()

        } catch (ex: Exception) {
            preferenceRepository.setCurrentLatitude(IPreferenceRepository.CURRENT_LATITUDE_DEFAULT_VALUE)
            preferenceRepository.setCurrentLongitude(IPreferenceRepository.CURRENT_LONGITUDE_DEFAULT_VALUE)
        }

        if(lat != null && log != null) {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val list = geocoder.getFromLocation(
                        lat, log, 1)
                if (list != null && list.size > 0) {
                    val address = list[0]
                    // sending back first address line and locality
                    addressResult = address.getAddressLine(0) + ", " + address.locality
                }
            } catch (e: IOException) {
               Timber.d("Impossible to connect to Geocoder")
            }

        }

        return addressResult

    }


}