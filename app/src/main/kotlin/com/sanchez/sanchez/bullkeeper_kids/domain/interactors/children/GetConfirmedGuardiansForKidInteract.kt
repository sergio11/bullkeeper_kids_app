package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.children

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.extension.ToDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IChildrenService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.RetrofitException
import com.sanchez.sanchez.bullkeeper_kids.domain.models.*
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Get Confirmed Guardians For Kid Interact
 */
class GetConfirmedGuardiansForKidInteract
    @Inject constructor(
            private val appContext: Context,
            private val childrenService: IChildrenService,
            private val preferenceRepository: IPreferenceRepository,
            private val apiEndPointsHelper: ApiEndPointsHelper,
            retrofit: Retrofit): UseCase<List<KidGuardianEntity>, UseCase.None>(retrofit){


    private val NO_KID_GUARDIAN_FOUND_CODE_NAME = "NO_KID_GUARDIAN_FOUND"

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None): List<KidGuardianEntity>  =
            childrenService.getConfirmedGuardiansForKid(preferenceRepository.getPrefKidIdentity())
                    .await().data?.map { kidGuardian ->
                KidGuardianEntity().apply {
                            identity = kidGuardian.identity
                            isConfirmed = kidGuardian.isConfirmed
                            requestAt = kidGuardian.requestAt?.ToDateTime(appContext.getString(R.string.date_format_server_response))
                            guardian = kidGuardian.guardian?.let {
                                GuardianEntity().apply {
                                    identity = it.identity
                                    firstName = it.firstName
                                    lastName = it.lastName
                                    birthdate = it.birthdate?.ToDateTime(appContext.getString(R.string.date_format_server_response))
                                    age = it.age
                                    email = it.email
                                    phonePrefix = it.phonePrefix
                                    phoneNumber = it.phoneNumber
                                    fbId = it.fbId
                                    children = it.children
                                    locale = it.locale
                                    profileImage =  it.profileImage?.let {
                                        apiEndPointsHelper.getProfileUrl(it)
                                    }
                                    visible = it.visible
                                }
                            }
                            role = kidGuardian.role?.let { role -> GuardianRolesEnum.valueOf(role) } ?: GuardianRolesEnum.DATA_VIEWER
                        }
            } ?: ArrayList()


    /**
     * On Api Exception Ocurred
     */
    override fun onApiExceptionOcurred(apiException: RetrofitException, response: APIResponse<*>?): Failure {
        return if(response?.codeName != null
                && response.codeName.equals(NO_KID_GUARDIAN_FOUND_CODE_NAME))
            NoKidGuardianFoundFailure() else super.onApiExceptionOcurred(apiException, response)
    }

    /**
     * No Kid Guardian Found Failure
     */
    class NoKidGuardianFoundFailure: Failure.FeatureFailure()

}