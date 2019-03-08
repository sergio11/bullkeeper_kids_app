package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls


import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.CallDetailEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveCallDetailDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ICallsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.ICallDetailRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Add Call Details From Terminal Interact
 */
class AddCallDetailsFromTerminalInteract
    @Inject constructor(
            private val callService: ICallsService,
            private val callDetailsRepositoryImpl: ICallDetailRepository,
            private val preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit): UseCase<Unit, AddCallDetailsFromTerminalInteract.Params>(retrofit){


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): Unit {

        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()

        val callDetail = CallDetailEntity()
        callDetail.id = params.localId
        callDetail.phoneNumber = params.phoneNumber
        callDetail.callDayTime = params.callDayTime
        callDetail.callDuration = params.callDuration
        callDetail.callType = params.callType

        // Save Call Detail
        callDetailsRepositoryImpl.save(callDetail)

        val response = callService
                .addCallDetailsFromTerminal(kid, terminal,
                        SaveCallDetailDTO(callDetail.phoneNumber,
                                callDetail.callDayTime, callDetail.callDuration, callDetail.callType,
                                callDetail.id, kid, terminal))
                .await()

        response.httpStatus?.let {
            if(it == "OK") {
                response.data?.let {
                    callDetail.serverId = it.identity
                    callDetail.sync = 1
                    // Save Sync Call Detail
                    callDetailsRepositoryImpl.save(callDetail)
                }
            }
        }
    }

    /**
     * Params
     */
    data class Params(
            var localId: String? = null,
            var phoneNumber: String? = null,
            var callDayTime: String? = null,
            var callDuration: String? = null,
            var callType: String? = null
    )

}