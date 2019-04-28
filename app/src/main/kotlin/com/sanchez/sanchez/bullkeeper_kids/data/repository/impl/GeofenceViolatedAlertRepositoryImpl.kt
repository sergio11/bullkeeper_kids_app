package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.entity.GeofenceViolatedAlertEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceViolatedAlertRepository
import io.realm.Realm
import timber.log.Timber

/**
    Geofence Violated Alert Repository
 **/
class GeofenceViolatedAlertRepositoryImpl: SupportRepositoryImpl<GeofenceViolatedAlertEntity>(), IGeofenceViolatedAlertRepository {

    /**
     * Delete Model
     * @param model
     */
    override fun delete(model: GeofenceViolatedAlertEntity) {
        Timber.d("Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        val geofenceAlertToDelete = realm.where(GeofenceViolatedAlertEntity::class.java)
                .equalTo("timestamp", model.timestamp)
                .findFirst()
        realm.executeTransaction {
            geofenceAlertToDelete?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Delete Model List
     * @param modelList
     */
    override fun delete(modelList: List<GeofenceViolatedAlertEntity>) {
        for(model in modelList) delete(model)
    }

    /**
     * Delete All
     */
    override fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.delete(GeofenceViolatedAlertEntity::class.java)
        }
        realm.close()
    }

    /**
     * List
     */
    override fun list(): List<GeofenceViolatedAlertEntity> {
        val realm = Realm.getDefaultInstance()
        val geofencesAlertList =
                realm.copyFromRealm(
                        realm.where(GeofenceViolatedAlertEntity::class.java).findAll())
        realm.close()
        return geofencesAlertList
    }

    /**
     * Find By Kid
     * @param kid
     */
    override fun findByKid(kid: String): List<GeofenceViolatedAlertEntity>? {
        val realm = Realm.getDefaultInstance()
        val realmResult = realm.where(GeofenceViolatedAlertEntity::class.java)
                .equalTo("kid", kid).findAll()
        var geofenceAlertList: List<GeofenceViolatedAlertEntity>? = null
        if(realmResult != null)
            geofenceAlertList = realm.copyFromRealm(realmResult)
        realm.close()
        return geofenceAlertList
    }


    /**
     * Delete By Kid
     * @param kid
     */
    override fun deleteByKid(kid: String) {
        val realm = Realm.getDefaultInstance()
        val geofenceAlertsToDelete = realm.where(GeofenceViolatedAlertEntity::class.java)
                .equalTo("kid", kid)
                .findAll()
        realm.executeTransaction {
            geofenceAlertsToDelete?.deleteAllFromRealm()
        }
        realm.close()
    }

}