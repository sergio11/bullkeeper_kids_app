package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal

import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ITerminalService
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

        return terminalEntity

    }


    /**
     * Params
     */
    data class Params(
            val  kid: String,
            val identityOrDeviceId: String
    )


}