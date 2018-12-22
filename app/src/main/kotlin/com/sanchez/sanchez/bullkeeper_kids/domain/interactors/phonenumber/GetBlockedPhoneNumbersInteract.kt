package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.phonenumber

import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.PhoneNumberBlockedEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IPhoneNumberService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPhoneNumberRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Get Blocked Phone Numbers Interact
 */
class GetBlockedPhoneNumbersInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val preferenceRepository: IPreferenceRepository,
            private val phoneNumberService: IPhoneNumberService,
            private val phoneNumberRepository: IPhoneNumberRepository
    ): UseCase<Unit, UseCase.None>(retrofit) {


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None) {

        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()
        // Get Phone Number Blocked
        val response = phoneNumberService.getPhoneNumberBlocked(kid, terminal).await()

        response.data?.map {
            PhoneNumberBlockedEntity(it.identity,
                    it.blockedAt, it.phoneNumber)
        }?.let { phoneNumbersBlockedToSave ->
            phoneNumberRepository.save(phoneNumbersBlockedToSave)
        }
    }
}