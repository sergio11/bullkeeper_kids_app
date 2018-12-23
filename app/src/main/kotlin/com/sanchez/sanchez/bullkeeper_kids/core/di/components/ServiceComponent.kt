package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.*
import com.sanchez.sanchez.bullkeeper_kids.presentation.services.MonitoringService
import dagger.Component
import javax.inject.Singleton

/**
 * Service Component
 */
@Singleton
@Component(modules = [ ApplicationModule::class, GlobalServiceModule::class,
    PackagesModule::class, NetModule::class, MonitoringModule::class,
    PhoneNumberBlockedModule::class, SmsModule::class, CallDetailsModule::class,
    ContactsModule::class, ScheduledBlocksModule::class])
interface ServiceComponent {

    /**
     * Inject into Monitoring Service
     */
    fun inject(service: MonitoringService)
}