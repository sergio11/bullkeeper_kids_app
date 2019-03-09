package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal

import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveTerminalStatusDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ITerminalService
import com.sanchez.sanchez.bullkeeper_kids.domain.models.TerminalStatusEnum
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Save Terminal Status Interact
 */
class SaveTerminalStatusInteract
    @Inject constructor(retrofit: Retrofit,
                        private val terminalService: ITerminalService):
        UseCase<Unit, SaveTerminalStatusInteract.Params>(retrofit) {

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params) {
        Preconditions.checkNotNull(params, "Params can not be null")

        terminalService
                .saveTerminalStatus(params.kid, params.terminal, SaveTerminalStatusDTO(
                        kid = params.kid,
                        terminal = params.terminal,
                        status = params.status.name)).await()

    }


    /**
     * Params
     */
    data class Params(
            val kid: String,
            val terminal: String,
            val status: TerminalStatusEnum
    )


}