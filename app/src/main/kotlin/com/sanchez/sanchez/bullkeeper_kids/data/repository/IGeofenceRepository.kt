package com.sanchez.sanchez.bullkeeper_kids.data.repository

import com.sanchez.sanchez.bullkeeper_kids.data.entity.GeofenceEntity

/**
 * Geofence Repository
 */
interface IGeofenceRepository: ISupportRepository<GeofenceEntity> {

    /**
     * Find By Identity
     */
    fun findById(id: String): GeofenceEntity?

    /**
     * Delete By Id
     */
    fun deleteById(ids: List<String>)

    /**
     * Find By Ids
     */
    fun findByIds(ids: List<String>): List<GeofenceEntity>
}