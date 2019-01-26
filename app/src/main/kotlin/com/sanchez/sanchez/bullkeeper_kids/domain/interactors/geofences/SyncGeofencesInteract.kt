package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences

import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.GeofenceEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IGeofencesService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGeofenceService
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Sync Geofences By Kid Interact
 */
class SyncGeofencesInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val preferenceRepository: IPreferenceRepository,
            private val geofencesService: IGeofencesService,
            private val geofencesRepository: IGeofenceRepository,
            private val deviceGeofenceService: IDeviceGeofenceService
    ): UseCase<Int, UseCase.None>(retrofit) {


    /**
     * Get Geofences To Create
     */
    private fun getGeofencesToCreate(
            localGeofenceList: List<GeofenceEntity>,
            serverGeofenceList: List<GeofenceEntity>
    ): List<GeofenceEntity> {

        // Geofences To Create
        var geofencesToCreate = arrayListOf<GeofenceEntity>()

        for(serverGeofence in serverGeofenceList) {
            var geofenceFound = false
            for(localGeofence in localGeofenceList) {
                if(!serverGeofence.identity.isNullOrEmpty() &&
                        serverGeofence.identity.equals(localGeofence.identity)) {
                    geofenceFound = true
                    /*serverGeofence.updateAt?.let {
                        if(it.after(localGeofence.updateAt)) {
                            geofencesToCreate.add(serverGeofence)
                        }
                    }*/
                    break
                }
            }

            if(!geofenceFound)
                geofencesToCreate.add(serverGeofence)

        }

        return geofencesToCreate
    }

    /**
     * Get Geofences To Remove
     */
    private fun getGeofencesToRemove(
            localGeofenceList: List<GeofenceEntity>,
            serverGeofenceList: List<GeofenceEntity>
    ): List<GeofenceEntity> {

        // Geofences To Remove
        var geofencesToRemove = arrayListOf<GeofenceEntity>()

        for(localGeofence in localGeofenceList) {
            var isFound = false
            for(serverGeofence in serverGeofenceList) {
                if(!localGeofence.identity.isNullOrEmpty() &&
                        localGeofence.identity.equals(serverGeofence.identity)){
                   /* localGeofence.updateAt?.let {
                        if(!it.before(serverGeofence.updateAt)){
                            isFound = true
                        }
                    }*/
                    isFound = true
                    break
                }
            }
            if(!isFound)
                geofencesToRemove.add(localGeofence)
        }

        return geofencesToRemove
    }

    /**
     * Sync Geofence
     */
    private suspend fun syncGeofences(): Pair<List<GeofenceEntity>, List<GeofenceEntity>> {

        // Get Kid
        val kid = preferenceRepository.getPrefKidIdentity()

        // Geofences Saved List
        val geofencesToSave = ArrayList<GeofenceEntity>()
        // Geofences To Remove
        val geofencesToRemove = ArrayList<GeofenceEntity>()

        // Get Geofences Saved List
        val localGeofencesSavedList = geofencesRepository.list()

        // Get Geofences
        val response =
                geofencesService.getGeofencesByKid(kid).await()

        val serverGeofenceList = response.data?.map {
            GeofenceEntity(it.identity, it.name, it.lat, it.log,
                    it.radius, it.type, it.kid)
        }?.toList()

        if(serverGeofenceList.isNullOrEmpty() && localGeofencesSavedList.isNotEmpty()) {
            geofencesRepository.deleteAll()
            geofencesToRemove.addAll(localGeofencesSavedList)
        } else if(serverGeofenceList != null && serverGeofenceList.isNotEmpty() && localGeofencesSavedList.isNullOrEmpty()) {
            geofencesRepository.save(serverGeofenceList)
            geofencesToSave.addAll(serverGeofenceList)
        } else {

            // Get Geofences To Remove
            geofencesToRemove.addAll(
                    getGeofencesToRemove(localGeofencesSavedList, serverGeofenceList!!))
            // Delete Geofences
            geofencesRepository.delete(geofencesToRemove)
            // Get Geofences To Create
            geofencesToSave.addAll(
                    getGeofencesToCreate(localGeofencesSavedList, serverGeofenceList))

            geofencesRepository.save(geofencesToSave)
        }

        return Pair(geofencesToSave, geofencesToRemove)
    }


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None): Int {

        // Sync Geofences
        val geofencesSync = syncGeofences()

        val geofencesSyncTotal = geofencesSync.first.size + geofencesSync.second.size

        // Delete Geofences On Terminal
        deviceGeofenceService.deleteGeofences(geofencesSync.second
                .filter { !it.identity.isNullOrEmpty() }.map { it.identity!! })

        // Configure Geofences on terminal
        deviceGeofenceService.addGeofence(geofencesSync.first)

        return geofencesSyncTotal

    }
}