package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.extension.ToDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.RetrofitException
import com.sanchez.sanchez.bullkeeper_kids.domain.models.*
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Get Conversation For Members Interact
 */
class GetConversationForMembersInteract
    @Inject constructor(
        private val context: Context,
        private val conversationService: IConversationService,
        retrofit: Retrofit): UseCase<ConversationEntity, GetConversationForMembersInteract.Params>(retrofit){


    private val CONVERSATION_NOT_FOUND_CODE_NAME = "CONVERSATION_NOT_FOUND_EXCEPTION"

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): ConversationEntity =
            conversationService.getConversationForMembers(params.memberOne, params.memberTwo).await().data?.let {
                return ConversationEntity().apply {
                    identity = it.identity
                    messagesCount = it.messagesCount
                    createAt = it.createAt?.ToDateTime(
                        context.getString(R.string.date_format_server_response)
                    )
                    updateAt = it.updateAt?.ToDateTime(
                            context.getString(R.string.date_format_server_response)
                    )
                    memberOne = PersonEntity().apply {
                        identity = it.memberOne?.identity
                        firstName = it.memberOne?.firstName
                        lastName = it.memberOne?.lastName
                        profileImage = it.memberOne?.profileImage
                    }
                    memberTwo = PersonEntity().apply {
                        identity = it.memberTwo?.identity
                        firstName = it.memberTwo?.firstName
                        lastName = it.memberTwo?.lastName
                        profileImage = it.memberTwo?.profileImage
                    }
                    lastMessage = it.lastMessage
                    lastMessageForMemberOne = it.lastMessageForMemberOne
                    lastMessageForMemberTwo = it.lastMessageForMemberTwo
                    pendingMessagesForMemberOne = it.pendingMessagesForMemberOne
                    pendingMessagesForMemberTwo = it.pendingMessagesForMemberTwo
                }
        } ?: ConversationEntity()


    /**
     * On Api Exception Ocurred
     */
    override fun onApiExceptionOcurred(apiException: RetrofitException, response: APIResponse<*>?): Failure {
        return if(response?.codeName != null
                && response.codeName.equals(CONVERSATION_NOT_FOUND_CODE_NAME))
            GetConversationForMembersInteract.ConversationNotFoundFailure()
        else super.onApiExceptionOcurred(apiException, response)
    }

    /**
     * Params
     */
    data class Params(

            /**
             * Member One
             */
            var memberOne: String,

            /**
             * Member Two
             */
            var memberTwo: String
    )


    // Conversation Not Found Failure
    class ConversationNotFoundFailure: Failure.FeatureFailure()

}