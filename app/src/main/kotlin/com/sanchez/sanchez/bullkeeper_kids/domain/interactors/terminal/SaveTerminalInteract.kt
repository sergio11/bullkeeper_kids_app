package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal

import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddTerminalDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ITerminalService
import com.sanchez.sanchez.bullkeeper_kids.domain.models.TerminalEntity
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Save Terminal Interact
 */
class SaveTerminalInteract
    @Inject constructor(retrofit: Retrofit,
                        private val terminalService: ITerminalService):
        UseCase<TerminalEntity, SaveTerminalInteract.Params>(retrofit) {

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): TerminalEntity {
        Preconditions.checkNotNull(params, "Params can not be null")

        val saveTerminal = AddTerminalDTO(
                appVersionCode = params.appVersionCode,
                appVersionName = params.appVersionName,
                codeName = params.codeName,
                deviceName = params.deviceName,
                deviceId = params.deviceId,
                manufacturer = params.manufacturer,
                marketName = params.marketName,
                model = params.model,
                osVersion = params.osVersion,
                sdkVersion = params.sdkVersion,
                kid = params.kidId)

        val response = terminalService
                .saveTerminal(params.kidId, saveTerminal).await()


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
        return terminalEntity

    }


    /**
     * Params
     */
    data class Params(
            val kidId: String,
            val appVersionCode: String,
            val appVersionName: String,
            val name: String,
            val codeName: String,
            val deviceName: String,
            val deviceId: String,
            val manufacturer: String,
            val marketName: String,
            val model: String,
            val osVersion: String,
            val sdkVersion: String

    )


}