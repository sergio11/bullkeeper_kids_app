package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.extension.ToDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
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
        private val conversationService: IConversationService,
        retrofit: Retrofit): UseCase<List<MessageEntity>, GetConversationMessagesInteract.Params>(retrofit){

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): List<MessageEntity> {

        val response =
                conversationService.getConversationMessages(params.kid).await()

        return response.data?.map {
            MessageEntity().apply {
                identity = it.identity
                viewed = it.viewed
                text = it.text
                conversation = it.conversation
                from = PersonEntity(
                        identity = it.from?.identity,
                        firstName = it.from?.firstName,
                        lastName = it.from?.lastName,
                        profileImage = it.from?.profileImage
                )
                to = PersonEntity(
                        identity = it.to?.identity,
                        firstName = it.to?.firstName,
                        lastName = it.to?.lastName,
                        profileImage = it.to?.profileImage
                )
                createAt = it.createAt?.ToDateTime(context
                        .getString(R.string.date_time_format))
            }
        }?.toList() ?: ArrayList()

    }


    /**
     * Params
     */
    data class Params(

            /**
             * Kid
             */
            var kid: String
    )


}