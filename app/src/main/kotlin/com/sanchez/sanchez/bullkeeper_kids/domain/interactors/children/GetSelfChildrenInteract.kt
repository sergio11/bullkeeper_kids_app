package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.children

import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.TerminalDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IParentsService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.RetrofitException
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SchoolEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SonEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.TerminalEntity
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Get Self Children Interact
 */
class GetSelfChildrenInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val parentService: IParentsService,
            private val apiEndPointsHelper: ApiEndPointsHelper): UseCase<List<SonEntity>, UseCase.None>(retrofit){


    private val NO_CHILDREN_FOUND_CODE_NAME = "NO_CHILDREN_FOUND"


    /**
     * Map Terminals
     */
    private fun mapTerminals(terminalsDTO: List<TerminalDTO>?): List<TerminalEntity>?
        = terminalsDTO?.map { terminalDTO -> TerminalEntity(terminalDTO.identity,
            terminalDTO.appVersionName, terminalDTO.appVersionCode, terminalDTO.osVersion,
            terminalDTO.sdkVersion, terminalDTO.manufacturer, terminalDTO.marketName, terminalDTO.model,
            terminalDTO.codeName, terminalDTO.deviceName)  }

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None): List<SonEntity> {

        val response = parentService.getSelfChildren().await()

        return response.data?.map { sonDTO ->
            SonEntity(sonDTO.identity, sonDTO.firstName, sonDTO.lastName,
                    sonDTO.birthdate, sonDTO.age,
                    SchoolEntity(sonDTO.schoolDTO?.identity, sonDTO.schoolDTO?.name,
                            sonDTO.schoolDTO?.residence, sonDTO.schoolDTO?.latitude,
                            sonDTO.schoolDTO?.longitude, sonDTO.schoolDTO?.province,
                            sonDTO.schoolDTO?.tfno, sonDTO.schoolDTO?.email),
                    if(sonDTO.profileImage != null)
                        apiEndPointsHelper.getSonProfileUrl(sonDTO.profileImage!!)
                    else null, mapTerminals(sonDTO.terminals))
        }!!

    }


    /**
     * On Api Exception Ocurred
     */
    override fun onApiExceptionOcurred(apiException: RetrofitException, response: APIResponse<*>): Failure {
        return if(response.codeName?.equals(NO_CHILDREN_FOUND_CODE_NAME)!!)
            NoChildrenFoundFailure() else super.onApiExceptionOcurred(apiException, response)
    }


    class NoChildrenFoundFailure: Failure.FeatureFailure()

}