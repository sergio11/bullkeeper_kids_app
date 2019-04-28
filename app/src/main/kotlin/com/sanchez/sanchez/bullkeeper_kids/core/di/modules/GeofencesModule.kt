package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IGeofencesService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceViolatedAlertRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.GeofenceRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.GeofenceViolatedAlertRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences.*
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
     * Provide Geofence Violated Alert Repository
     */
    @Provides
    @Singleton
    fun provideGeofenceViolatedAlertRepository(): IGeofenceViolatedAlertRepository
            = GeofenceViolatedAlertRepositoryImpl()

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
            appContext: Context,
            retrofit: Retrofit,
            preferencesRepository: IPreferenceRepository,
            geofencesService: IGeofencesService,
            geofenceRepository: IGeofenceRepository,
            deviceGeofenceService: IDeviceGeofenceService
    ): SyncGeofencesInteract
        = SyncGeofencesInteract(retrofit, appContext, preferencesRepository,
            geofencesService, geofenceRepository, deviceGeofenceService)


    /**
     * Provide Add Geofence Interact
     */
    @Provides
    @Singleton
    fun provideSaveGeofenceInteract(
            appContext: Context,
            deviceGeofenceService: IDeviceGeofenceService,
            geofenceRepository: IGeofenceRepository,
            retrofit: Retrofit
    ): SaveGeofenceInteract
        = SaveGeofenceInteract(appContext, deviceGeofenceService,
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

    /**
     * Provide Save Geofence Alert Interact
     */
    @Provides
    @Singleton
    fun provideSaveGeofenceAlertInteract(
            geofencesService: IGeofencesService,
            retrofit: Retrofit
    ): SaveGeofenceAlertInteract
        = SaveGeofenceAlertInteract(geofencesService, retrofit)

    /**
     * Provide Update Geofence Status Interact
     */
    @Provides
    @Singleton
    fun provideUpdateGeofenceStatusInteract(geofenceRepository: IGeofenceRepository,
                                            retrofit: Retrofit): UpdateGeofenceStatusInteract
        = UpdateGeofenceStatusInteract(geofenceRepository, retrofit)


    /**
     * Provide Notify Pending Geofence
     */
    @Provides
    @Singleton
    fun provideNotifyPendingGeofenceAlertsInteract(geofencesService: IGeofencesService,
                                                   geoViolatedAlertRepository: IGeofenceViolatedAlertRepository,
                                                   retrofit: Retrofit): NotifyPendingGeofenceAlertsInteract
        = NotifyPendingGeofenceAlertsInteract(geofencesService, geoViolatedAlertRepository, retrofit)

    /**
     * Provide Active Geofences Interact
     */
    @Provides
    @Singleton
    fun provideActiveGeofencesInteract(retrofit: Retrofit,
                                       geofenceRepository: IGeofenceRepository,
                                       deviceGeofenceService: IDeviceGeofenceService)
        = ActiveGeofencesInteract(retrofit, geofenceRepository, deviceGeofenceService)
}