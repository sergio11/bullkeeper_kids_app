package com.sanchez.sanchez.bullkeeper_kids.core.di.components


import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ApplicationModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.GeofencesModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.GlobalServiceModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.NetModule
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.GeofenceBroadcastReceiver
import dagger.Component
import javax.inject.Singleton

/**
 * Geofence Component
 */
@Singleton
@Component(
        modules = [GeofencesModule::class, GlobalServiceModule::class, ApplicationModule::class,
            NetModule::class])
interface GeofenceComponent {


    /**
     * Inject into Geofence Broadcast Receiver
     */
    fun inject(geofenceBroadcastReceiver: GeofenceBroadcastReceiver)
}