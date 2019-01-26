package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.entity.GeofenceEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceRepository
import io.realm.Realm
import timber.log.Timber

/**
 * Geofence Repository Impl
 */
class GeofenceRepositoryImpl: SupportRepositoryImpl<GeofenceEntity>(), IGeofenceRepository {

    /**
     * Find By Id
     */
    override fun findById(id: String): GeofenceEntity? {
        Timber.d("Find By -> $id")
        val realm = Realm.getDefaultInstance()
        val realmResult = realm.where(GeofenceEntity::class.java)
                .equalTo("identity", id).findFirst()
        var geofenceIdentity: GeofenceEntity? = null
        if(realmResult != null)
            geofenceIdentity = realm.copyFromRealm(realmResult)
        realm.close()
        return geofenceIdentity
    }

    /**
     * Delete
     */
    override fun delete(model: GeofenceEntity) {
        Timber.d("Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        // Find Geofence
        val geofenceToDelete = realm.where(GeofenceEntity::class.java)
                .equalTo("identity", model.identity)
                .findFirst()
        realm.executeTransaction {
            geofenceToDelete?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Find By Ids
     */
    override fun findByIds(ids: List<String>): List<GeofenceEntity> {
        val realm = Realm.getDefaultInstance()
        // Find Geofence
        val geofencesList = realm.copyFromRealm(
                realm.where(GeofenceEntity::class.java)
                    .`in`("identity", ids.toTypedArray())
                    .findAll())
        realm.close()
        return geofencesList
    }

    /**
     * Delete
     */
    override fun delete(modelList: List<GeofenceEntity>) {
        for(model in modelList) delete(model)
    }

    /**
     * Delete All
     */
    override fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.delete(GeofenceEntity::class.java)
        }
        realm.close()
    }

    /**
     * List
     */
    override fun list(): List<GeofenceEntity> {
        val realm = Realm.getDefaultInstance()
        val geofencesList =
                realm.copyFromRealm(
                        realm.where(GeofenceEntity::class.java).findAll())
        realm.close()
        return geofencesList
    }

    /**
     * Delete By Id
     */
    override fun deleteById(ids: List<String>) {
        val realm = Realm.getDefaultInstance()
        // Find Geofence
        val geofenceToDelete = realm.where(GeofenceEntity::class.java)
                .`in`("identity", ids.toTypedArray())
                .findFirst()
        realm.executeTransaction {
            geofenceToDelete?.deleteFromRealm()
        }
        realm.close()
    }
}