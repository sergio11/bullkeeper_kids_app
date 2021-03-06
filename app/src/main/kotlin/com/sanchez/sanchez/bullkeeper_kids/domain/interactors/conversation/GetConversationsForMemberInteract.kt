package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.extension.ToDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.RetrofitException
import com.sanchez.sanchez.bullkeeper_kids.domain.models.*
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Get Conversation For Member Interact
 */
class GetConversationsForMemberInteract
    @Inject constructor(
            private val context: Context,
            private val apiEndPointsHelper: ApiEndPointsHelper,
            private val conversationService: IConversationService,
            retrofit: Retrofit): UseCase<List<ConversationEntity>, GetConversationsForMemberInteract.Params>(retrofit){


    private val NO_CONVERSATION_FOUND_CODE_NAME = "NO_CONVERSATIONS_FOUND"


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): List<ConversationEntity>  =
        conversationService.getConversationsForMember(params.id).await().data?.map { ConversationEntity().apply {
            identity = it.identity
            messagesCount = it.messagesCount
            createAt = it.createAt?.ToDateTime(context.getString(R.string.date_format_server_response))
            updateAt = it.updateAt?.ToDateTime(context.getString(R.string.date_format_server_response))
            memberOne = PersonEntity().apply {
                identity = it.memberOne?.identity
                firstName = it.memberOne?.firstName
                lastName = it.memberOne?.lastName
                profileImage = it.memberOne?.profileImage?.let {
                    apiEndPointsHelper.getProfileUrl(it)
                }
            }
            memberTwo = PersonEntity().apply {
                identity = it.memberTwo?.identity
                firstName = it.memberTwo?.firstName
                lastName = it.memberTwo?.lastName
                profileImage = it.memberTwo?.profileImage?.let {
                    apiEndPointsHelper.getProfileUrl(it)
                }
            }
            lastMessage = it.lastMessage
            lastMessageForMemberOne = it.lastMessageForMemberOne
            lastMessageForMemberTwo = it.lastMessageForMemberTwo
            pendingMessagesForMemberOne = it.pendingMessagesForMemberOne
            pendingMessagesForMemberTwo = it.pendingMessagesForMemberTwo
        } } ?: ArrayList()

    /**
     * On Api Exception Ocurred
     */
    override fun onApiExceptionOcurred(apiException: RetrofitException, response: APIResponse<*>?): Failure {
        return if(response?.codeName != null
                && response.codeName.equals(NO_CONVERSATION_FOUND_CODE_NAME))
            NoConversationFoundFailure() else super.onApiExceptionOcurred(apiException, response)
    }


    /**
     * Params
     */
    data class Params(

            /**
             * id
             */
            var id: String
    )

    // No Conversation Found Failure
    class NoConversationFoundFailure: Failure.FeatureFailure()

}