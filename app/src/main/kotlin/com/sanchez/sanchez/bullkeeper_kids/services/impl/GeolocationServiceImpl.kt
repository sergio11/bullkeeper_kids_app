package com.sanchez.sanchez.bullkeeper_kids.services.impl

import android.content.Context
import android.provider.Settings
import com.sanchez.sanchez.bullkeeper_kids.services.IGeolocationService

/**
 * Geolocation Service
 */
class GeolocationServiceImpl(private val context: Context): IGeolocationService {

    /**
     * Is High Accuraccy Location Enabled
     */
    override fun isHighAccuraccyLocationEnabled(): Boolean {
        var locationMode = 0
        try {
            locationMode = Settings.Secure.getInt(context.contentResolver,
                    Settings.Secure.LOCATION_MODE)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        return locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY
    }
}