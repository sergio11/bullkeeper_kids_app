package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.kidrequest

import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddKidRequestDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveCurrentLocationDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IKidRequestService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.RetrofitException
import com.sanchez.sanchez.bullkeeper_kids.domain.models.RequestTypeEnum
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject

/**
 * Send Request Request Interact
 * Return Expired at
 */
class SendRequestInteract
    @Inject constructor(
            private val kidRequestService: IKidRequestService,
            private val preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit): UseCase<Date, SendRequestInteract.Params>(retrofit) {


    private val PREVIOUS_REQUEST_HAS_NOT_EXPIRED = "PREVIOUS_REQUEST_HAS_NOT_EXPIRED"

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: SendRequestInteract.Params): Date {

        // Kid and terminal
        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()

        // Latitude or longitude
        val latitude = preferenceRepository.getCurrentLatitude().toDoubleOrNull()
        val longitude = preferenceRepository.getCurrentLongitude().toDoubleOrNull()

        // Create Request
        val sosRequest = AddKidRequestDTO()
        sosRequest.kid = kid
        sosRequest.terminal = terminal
        sosRequest.type = params.type
        sosRequest.location = SaveCurrentLocationDTO(latitude, longitude)

        val response =
                kidRequestService.addRequestForKid(kid, sosRequest).await()

        return response.data?.expiredAt!!

    }

    /**
     * On Api Exception Ocurred
     */
    override fun onApiExceptionOcurred(apiException: RetrofitException, response: APIResponse<*>?): Failure {
        return if(response?.codeName != null
                && response.codeName.equals(PREVIOUS_REQUEST_HAS_NOT_EXPIRED))
            PreviousRequestHasNotExpiredException() else super.onApiExceptionOcurred(apiException, response)
    }

    /**
     * Previous Request Has not Expired Exception
     */
    class PreviousRequestHasNotExpiredException: Failure.FeatureFailure()


    /**
     * Params
     */

    data class Params(val type: String)

}