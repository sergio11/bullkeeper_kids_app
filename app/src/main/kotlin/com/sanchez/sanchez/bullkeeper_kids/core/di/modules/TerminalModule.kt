package com.sanchez.sanchez.bullkeeper_kids.core.di.modules


import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ITerminalService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.*
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.GetTerminalDetailInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.SaveTerminalInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.UnlinkTerminalInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Terminal Module
 */
@Module
class TerminalModule {


    /**
     * Provide Terminal Service
     */
    @Provides
    @Singleton
    fun provideTerminalService(retrofit: Retrofit): ITerminalService
        = retrofit.create(ITerminalService::class.java)

    /**
     * Provide Save Terminal Interact
     */
    @Provides
    @Singleton
    fun provideSaveTerminalInteract(retrofit: Retrofit, terminalService: ITerminalService)
        = SaveTerminalInteract(retrofit, terminalService)


    /**
     * Provide Get Terminal Detail Interact
     */
    @Provides
    @Singleton
    fun provideGetTerminalDetailInteract(retrofit: Retrofit, terminalService: ITerminalService)
        = GetTerminalDetailInteract(retrofit, terminalService)


    /**
     * Provide Unlink Terminal Interact
     */
    @Provides
    @Singleton
    fun provideUnlinkTerminalInteract(
            retrofit: Retrofit,
            appAllowedByScheduledRepository: IAppAllowedByScheduledRepository,
            appsInstalledRepository: IAppsInstalledRepository,
            callsRepository: ICallDetailRepository,
            contactsRepository: IContactRepository,
            funTimeDayScheduledRepository: IFunTimeDayScheduledRepository,
            geofenceRepository: IGeofenceRepository,
            packageUsageStatsRepository: IPackageUsageStatsRepository,
            phoneNumberRepository: IPhoneNumberRepository,
            scheduledBlocksRepository: IScheduledBlocksRepository,
            smsRepository: ISmsRepository,
            preferencesRepository: IPreferenceRepository
    ): UnlinkTerminalInteract
            = UnlinkTerminalInteract(retrofit, appAllowedByScheduledRepository, appsInstalledRepository,
            callsRepository, contactsRepository, funTimeDayScheduledRepository, geofenceRepository,
            packageUsageStatsRepository, phoneNumberRepository, scheduledBlocksRepository,
            smsRepository, preferencesRepository)


}