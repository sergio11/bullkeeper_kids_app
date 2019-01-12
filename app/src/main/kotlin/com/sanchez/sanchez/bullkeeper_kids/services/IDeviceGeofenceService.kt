package com.sanchez.sanchez.bullkeeper_kids.services

import com.sanchez.sanchez.bullkeeper_kids.data.entity.GeofenceEntity

/**
 * Device Geofence Service
 */
interface IDeviceGeofenceService {

    /**
     * Add Geofence
     */
    fun addGeofence(geofenceEntityList: List<GeofenceEntity>)

    /**
     * Add Geofence
     */
    fun addGeofence(geofenceEntity: GeofenceEntity)

    /**
     * Delete All Geofences
     */
    fun deleteAllGeofences()

    /**
     * Delete Geofence By Id
     */
    fun deleteGeofenceById(identity: String)

    /**
     * Delete Geofences
     */
    fun deleteGeofences(ids: List<String>)

}