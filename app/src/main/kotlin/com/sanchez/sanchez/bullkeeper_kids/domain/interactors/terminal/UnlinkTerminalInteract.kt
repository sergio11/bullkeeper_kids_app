package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal

import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.*
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.ScheduledBlocksRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Unlink Terminal
 */
class UnlinkTerminalInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val preferencesRepository: IPreferenceRepository,
            private val appsInstalledRepository: IAppsInstalledRepository,
            private val smsRepository: ISmsRepository,
            private val callsRepository: ICallDetailRepository,
            private val contactsRepository: IContactRepository,
            private val phoneNumberRepositoryImpl: IPhoneNumberRepository,
            private val scheduledBlocksRepositoryImpl: ScheduledBlocksRepositoryImpl,
            private val packageUsageStatsRepositoryImpl: IPackageUsageStatsRepository):
            UseCase<Unit, UseCase.None>(retrofit){


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None) {

        // Delete all Contacts
        contactsRepository.deleteAll()
        // Delete all sms
        smsRepository.deleteAll()
        // Delete All Calls
        callsRepository.deleteAll()
        // Delete all apps
        appsInstalledRepository.deleteAll()
        // Delete all phone numbers blocked
        phoneNumberRepositoryImpl.deleteAll()
        // Delete all scheduled blocks
        scheduledBlocksRepositoryImpl.deleteAll()
        // Delete All Usage Stats Repository Impl
        packageUsageStatsRepositoryImpl.deleteAll()

        // Reset Preferences
        preferencesRepository.setPrefKidIdentity(IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE)
        preferencesRepository.setPrefTerminalIdentity(IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE)
        preferencesRepository.setPrefDeviceId(IPreferenceRepository.CURRENT_DEVICE_ID_DEFAULT_VALUE)
        preferencesRepository.setPrefCurrentUserIdentity(IPreferenceRepository.CURRENT_USER_IDENTITY_DEFAULT_VALUE)
        preferencesRepository.setAuthToken(IPreferenceRepository.AUTH_TOKEN_DEFAULT_VALUE)
        preferencesRepository.setCameraEnabled(IPreferenceRepository.LOCK_CAMERA_DEFAULT_VALUE)
        preferencesRepository.setLockScreenEnabled(IPreferenceRepository.LOCK_SCREEN_DEFAULT_VALUE)
        preferencesRepository.setBedTimeEnabled(IPreferenceRepository.BED_TIME_DEFAULT_VALUE)
    }


}