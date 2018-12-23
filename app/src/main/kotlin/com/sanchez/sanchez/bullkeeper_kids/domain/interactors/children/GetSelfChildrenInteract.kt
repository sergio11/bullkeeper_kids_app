package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.children

import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.GuardianRolesEnum
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.TerminalDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IGuardiansService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.RetrofitException
import com.sanchez.sanchez.bullkeeper_kids.domain.models.*
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Get Self Children Interact
 */
class GetSelfChildrenInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val parentService: IGuardiansService,
            private val apiEndPointsHelper: ApiEndPointsHelper): UseCase<ChildrenOfSelfGuardianEntity, UseCase.None>(retrofit){


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
    override suspend fun onExecuted(params: None): ChildrenOfSelfGuardianEntity {

        val response = parentService.getSelfChildren().await()

        val childrenOfSelfParent = ChildrenOfSelfGuardianEntity()
        childrenOfSelfParent.confirmed = response.data?.confirmed
        childrenOfSelfParent.noConfirmed = response.data?.noConfirmed
        childrenOfSelfParent.total = response.data?.total

        val supervisedChildrenEntities = ArrayList<SupervisedChildrenEntity>()
        for(supervisedChildren in response.data?.supervisedChildrenList!!) {
            val supervisedChildrenEntity = SupervisedChildrenEntity()
            supervisedChildrenEntity.identity = supervisedChildren.identity
            supervisedChildrenEntity.role = GuardianRolesEnum.valueOf(supervisedChildren.role.orEmpty())
            supervisedChildrenEntity.kid = KidEntity()
            supervisedChildrenEntity.kid?.identity = supervisedChildren.kid?.identity
            supervisedChildrenEntity.kid?.firstName = supervisedChildren.kid?.firstName
            supervisedChildrenEntity.kid?.lastName = supervisedChildren.kid?.lastName
            supervisedChildrenEntity.kid?.age = supervisedChildren.kid?.age
            supervisedChildrenEntity.kid?.school = SchoolEntity()
            supervisedChildrenEntity.kid?.school?.identity = supervisedChildren.kid?.schoolDTO?.identity
            supervisedChildrenEntity.kid?.school?.name = supervisedChildren.kid?.schoolDTO?.name
            supervisedChildrenEntity.kid?.school?.residence = supervisedChildren.kid?.schoolDTO?.residence
            supervisedChildrenEntity.kid?.school?.latitude = supervisedChildren.kid?.schoolDTO?.latitude
            supervisedChildrenEntity.kid?.school?.longitude = supervisedChildren.kid?.schoolDTO?.longitude
            supervisedChildrenEntity.kid?.school?.province = supervisedChildren.kid?.schoolDTO?.province
            supervisedChildrenEntity.kid?.school?.tfno = supervisedChildren.kid?.schoolDTO?.tfno
            supervisedChildrenEntity.kid?.school?.email = supervisedChildren.kid?.schoolDTO?.email
            supervisedChildren.kid?.profileImage?.let{
                supervisedChildren.kid?.profileImage = apiEndPointsHelper.getKidProfileUrl(it)
            }
            supervisedChildrenEntity.kid?.terminals =    mapTerminals(supervisedChildren.kid?.terminals)
            supervisedChildrenEntities.add(supervisedChildrenEntity)
        }

        childrenOfSelfParent.supervisedChildrenList = supervisedChildrenEntities

        return childrenOfSelfParent
    }


    /**
     * On Api Exception Ocurred
     */
    override fun onApiExceptionOcurred(apiException: RetrofitException, response: APIResponse<*>?): Failure {
        return if(response?.codeName != null
                && response.codeName.equals(NO_CHILDREN_FOUND_CODE_NAME))
            NoChildrenFoundFailure() else super.onApiExceptionOcurred(apiException, response)
    }


    class NoChildrenFoundFailure: Failure.FeatureFailure()

}