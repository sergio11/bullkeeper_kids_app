package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IGeofencesService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.GeofenceRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences.DeleteAllGeofenceInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences.SaveGeofenceInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences.DeleteGeofenceInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences.SyncGeofencesInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGeofenceService
import com.sanchez.sanchez.bullkeeper_kids.services.impl.DeviceGeofenceServiceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Geofences Module
 */
@Module
class GeofencesModule {


    /**
     * Provide Geofences Service
     */
    @Provides
    @Singleton
    fun provideGeofencesService(retrofit: Retrofit): IGeofencesService
            = retrofit.create(IGeofencesService::class.java)

    /**
     * Provide Geofences Repository
     */
    @Provides
    @Singleton
    fun provideGeofencesRepository(): IGeofenceRepository
        = GeofenceRepositoryImpl()

    /**
     * Provide Geofence Service
     */
    @Provides
    @Singleton
    fun provideGeofenceService(appContext: Context): IDeviceGeofenceService
        = DeviceGeofenceServiceImpl(appContext)

    /**
     * Provide Sync Geofences Interact
     */
    @Provides
    @Singleton
    fun provideSyncGeofencesInteract(
            retrofit: Retrofit,
            preferencesRepository: IPreferenceRepository,
            geofencesService: IGeofencesService,
            geofenceRepository: IGeofenceRepository,
            deviceGeofenceService: IDeviceGeofenceService
    ): SyncGeofencesInteract
        = SyncGeofencesInteract(retrofit, preferencesRepository,
            geofencesService, geofenceRepository, deviceGeofenceService)


    /**
     * Provide Add Geofence Interact
     */
    @Provides
    @Singleton
    fun provideSaveGeofenceInteract(
            deviceGeofenceService: IDeviceGeofenceService,
            geofenceRepository: IGeofenceRepository,
            retrofit: Retrofit
    ): SaveGeofenceInteract
        = SaveGeofenceInteract(deviceGeofenceService,
            geofenceRepository, retrofit)

    /**
     * Provide Delete Geofence Interact
     */
    @Provides
    @Singleton
    fun provideDeleteGeofenceInteract(
            deviceGeofenceService: IDeviceGeofenceService,
            geofenceRepository: IGeofenceRepository,
            retrofit: Retrofit
    ): DeleteGeofenceInteract
        = DeleteGeofenceInteract(deviceGeofenceService,
            geofenceRepository, retrofit)

    /**
     * Provide Delete All Geofence Interact
     */
    @Provides
    @Singleton
    fun provideDeleteAllGeofenceInteract(
            deviceGeofenceService: IDeviceGeofenceService,
            geofenceRepository: IGeofenceRepository,
            retrofit: Retrofit
    ): DeleteAllGeofenceInteract
            = DeleteAllGeofenceInteract(deviceGeofenceService,
            geofenceRepository, retrofit)
}