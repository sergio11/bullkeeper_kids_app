package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal

import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.*
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Unlink Terminal
 */
class UnlinkTerminalInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val appAllowedByScheduledRepository: IAppAllowedByScheduledRepository,
            private val appsInstalledRepository: IAppsInstalledRepository,
            private val callsRepository: ICallDetailRepository,
            private val contactsRepository: IContactRepository,
            private val funTimeDayScheduledRepository: IFunTimeDayScheduledRepository,
            private val geofenceRepository: IGeofenceRepository,
            private val packageUsageStatsRepository: IPackageUsageStatsRepository,
            private val phoneNumberRepository: IPhoneNumberRepository,
            private val scheduledBlocksRepository: IScheduledBlocksRepository,
            private val smsRepository: ISmsRepository,
            private val preferencesRepository: IPreferenceRepository):
            UseCase<Unit, UseCase.None>(retrofit){


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None) {

        appAllowedByScheduledRepository.deleteAll()
        appsInstalledRepository.deleteAll()
        callsRepository.deleteAll()
        contactsRepository.deleteAll()
        funTimeDayScheduledRepository.deleteAll()
        geofenceRepository.deleteAll()
        packageUsageStatsRepository.deleteAll()
        phoneNumberRepository.deleteAll()
        scheduledBlocksRepository.deleteAll()
        smsRepository.deleteAll()

        // Reset Preferences
        preferencesRepository.setPrefKidIdentity(IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE)
        preferencesRepository.setPrefTerminalIdentity(IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE)
        preferencesRepository.setPrefDeviceId(IPreferenceRepository.CURRENT_DEVICE_ID_DEFAULT_VALUE)
        preferencesRepository.setPrefCurrentUserIdentity(IPreferenceRepository.CURRENT_USER_IDENTITY_DEFAULT_VALUE)
        preferencesRepository.setCameraEnabled(IPreferenceRepository.CAMERA_ENABLED_DEFAULT_VALUE)
        preferencesRepository.setScreenEnabled(IPreferenceRepository.SCREEN_ENABLED_DEFAULT_VALUE)
        preferencesRepository.setBedTimeEnabled(IPreferenceRepository.BED_TIME_DEFAULT_VALUE)
        preferencesRepository.setSettingsEnabled(IPreferenceRepository.SETTINGS_ENABLED_DEFAULT_VALUE)
        preferencesRepository.setPhoneCallsEnabled(IPreferenceRepository.PHONE_CALLS_ENABLED_DEFAULT_VALUE)
        preferencesRepository.setRemainingFunTime(IPreferenceRepository.REMAINING_FUN_TIME_DEFAULT_VALUE)
        preferencesRepository.setTimeBank(IPreferenceRepository.TIME_BANK_DEFAUL_VALUE)
    }


}