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
import com.sanchez.sanchez.bullkeeper_kids.domain.models.MessageEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.PersonEntity
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Get Conversation Messages Interact
 */
class GetConversationMessagesInteract
    @Inject constructor(
            private val context: Context,
            private val apiEndPointsHelper: ApiEndPointsHelper,
            private val conversationService: IConversationService,
            retrofit: Retrofit): UseCase<List<MessageEntity>, GetConversationMessagesInteract.Params>(retrofit){


    private val CONVERSATION_NOT_FOUND_CODE_NAME = "CONVERSATION_NOT_FOUND_EXCEPTION"
    private val NO_MESSAGES_FOUND_CODE_NAME = "NO_MESSAGES_FOUND"

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): List<MessageEntity> =
            conversationService.getConversationMessages(params.id).await().data?.map {
                MessageEntity().apply {
                    identity = it.identity
                    viewed = it.viewed
                    text = it.text
                    conversation = it.conversation
                    from = PersonEntity(
                            identity = it.from?.identity,
                            firstName = it.from?.firstName,
                            lastName = it.from?.lastName,
                            profileImage = it.from?.profileImage?.let {
                                apiEndPointsHelper.getProfileUrl(it)
                            }
                    )
                    to = PersonEntity(
                            identity = it.to?.identity,
                            firstName = it.to?.firstName,
                            lastName = it.to?.lastName,
                            profileImage = it.to?.profileImage?.let {
                                apiEndPointsHelper.getProfileUrl(it)
                            }
                    )
                    createAt = it.createAt?.ToDateTime(context
                            .getString(R.string.date_time_format_2))
                }
            }?.toList() ?: ArrayList()


    /**
     * On Api Exception Ocurred
     */
    override fun onApiExceptionOcurred(apiException: RetrofitException, response: APIResponse<*>?): Failure {
        return if(response?.codeName != null
                && response.codeName.equals(NO_MESSAGES_FOUND_CODE_NAME))
                NoMessagesFoundFailure()
            else if(response?.codeName != null
                && response.codeName.equals(CONVERSATION_NOT_FOUND_CODE_NAME))
                ConversationNotFoundFailure()
            else super.onApiExceptionOcurred(apiException, response)
    }

    /**
     * Params
     */
    data class Params(

            /**
             * Id
             */
            var id: String
    )


    // No Messages Found Failure
    class NoMessagesFoundFailure: Failure.FeatureFailure()

    // Conversation Not Found Failure
    class ConversationNotFoundFailure: Failure.FeatureFailure()
}