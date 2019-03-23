package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal

import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ITerminalService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.RetrofitException
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Detach Terminal Interact
 */
class DetachTerminalInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val terminalService: ITerminalService,
            private val preferenceRepository: IPreferenceRepository):
            UseCase<Unit, UseCase.None>(retrofit){

    private val NO_TERMINAL_FOUND_CODE_NAME = "NO_TERMINAL_FOUND_EXCEPTION"

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None) {

        if(preferenceRepository.getPrefKidIdentity()
                != IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE &&
                preferenceRepository.getPrefTerminalIdentity() !=
                IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE) {

            val kid = preferenceRepository.getPrefKidIdentity()
            val terminal = preferenceRepository.getPrefTerminalIdentity()

            terminalService.detachTerminal(kid, terminal).await()
        }
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
}