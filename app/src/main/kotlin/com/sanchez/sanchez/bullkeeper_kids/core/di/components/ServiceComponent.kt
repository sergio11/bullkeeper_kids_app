package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ApplicationModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.PackagesModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ServicesModule
import com.sanchez.sanchez.bullkeeper_kids.presentation.services.MonitoringService
import dagger.Component
import javax.inject.Singleton

/**
 * Service Component
 */
@Singleton
@Component(modules = [ ApplicationModule::class, ServicesModule::class,
    PackagesModule::class ])
interface ServiceComponent {

    /**
     * Inject into Monitoring Service
     */
    fun inject(service: MonitoringService)
}