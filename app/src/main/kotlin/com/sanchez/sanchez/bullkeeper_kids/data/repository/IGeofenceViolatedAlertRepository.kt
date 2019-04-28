package com.sanchez.sanchez.bullkeeper_kids.data.repository

import com.sanchez.sanchez.bullkeeper_kids.data.entity.GeofenceViolatedAlertEntity

/**
 * Geofence Violated Alert Repository
 */
interface IGeofenceViolatedAlertRepository: ISupportRepository<GeofenceViolatedAlertEntity> {

    /**
     * Find By Kid
     */
    fun findByKid(kid: String): List<GeofenceViolatedAlertEntity>?

    /**
     * Delete By Kid
     */
    fun deleteByKid(kid: String)
}