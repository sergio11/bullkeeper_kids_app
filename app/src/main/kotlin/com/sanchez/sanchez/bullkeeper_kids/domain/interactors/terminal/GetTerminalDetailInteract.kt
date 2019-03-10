package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal

import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ITerminalService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.RetrofitException
import com.sanchez.sanchez.bullkeeper_kids.domain.models.TerminalEntity
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Get Terminal Interact
 */
class GetTerminalDetailInteract
    @Inject constructor(retrofit: Retrofit,
                        private val terminalService: ITerminalService)
    : UseCase<TerminalEntity, GetTerminalDetailInteract.Params>(retrofit) {

    private val NO_TERMINAL_FOUND_CODE_NAME = "NO_TERMINAL_FOUND_EXCEPTION"

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): TerminalEntity {
        Preconditions.checkNotNull(params, "Params can not be null")

        // Get Terminal Detail by identity or device id
        val response = terminalService
                .getTerminalDetail(params.kid, params.identityOrDeviceId).await()

        val terminalEntity = TerminalEntity()
        terminalEntity.identity = response.data?.identity
        terminalEntity.appVersionCode = response.data?.appVersionCode
        terminalEntity.appVersionName = response.data?.appVersionName
        terminalEntity.codeName = response.data?.codeName
        terminalEntity.deviceName = response.data?.deviceName
        terminalEntity.deviceId = response.data?.deviceId
        terminalEntity.manufacturer = response.data?.manufacturer
        terminalEntity.marketName = response.data?.marketName
        terminalEntity.model = response.data?.model
        terminalEntity.osVersion = response.data?.osVersion
        terminalEntity.sdkVersion = response.data?.sdkVersion
        terminalEntity.kidId = response.data?.kidId

        response.data?.bedTimeEnabled?.let {
            terminalEntity.bedTimeEnabled = it
        }

        response.data?.screenEnabled?.let {
            terminalEntity.screenEnabled = it
        }

        response.data?.cameraEnabled?.let {
            terminalEntity.cameraEnabled = it
        }

        response.data?.settingsEnabled?.let {
            terminalEntity.settingsEnabled = it
        }

        return terminalEntity

    }


    /**
     * On Api Exception Ocurred
     */
    override fun onApiExceptionOcurred(apiException: RetrofitException, response: APIResponse<*>?): Failure {
        return if(response?.codeName != null
                && response.codeName.equals(NO_TERMINAL_FOUND_CODE_NAME))
            NoTerminalFoundFailure() else super.onApiExceptionOcurred(apiException, response)
    }

    /**
     * No Terminal Found Failure
     */
    class NoTerminalFoundFailure: Failure.FeatureFailure()

    /**
     * Params
     */
    data class Params(
            val  kid: String,
            val identityOrDeviceId: String
    )


}