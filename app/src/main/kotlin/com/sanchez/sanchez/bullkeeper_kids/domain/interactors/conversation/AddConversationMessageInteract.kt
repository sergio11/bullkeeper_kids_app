package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.extension.ToDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddMessageDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
import com.sanchez.sanchez.bullkeeper_kids.domain.models.MessageEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.PersonEntity
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Add Conversation Message Interact
 */
class AddConversationMessageInteract
    @Inject constructor(
        private val appContext: Context,
        private val conversationService: IConversationService,
        retrofit: Retrofit): UseCase<MessageEntity, AddConversationMessageInteract.Params>(retrofit){

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): MessageEntity {

        val response = conversationService.addMessage(params.conversation,
                AddMessageDTO(params.text, params.from, params.to)).await()

        return response.data?.let {

            val messageEntity = MessageEntity()
            messageEntity.identity = it.identity
            messageEntity.viewed = it.viewed
            messageEntity.text = it.text
            messageEntity.conversation = it.conversation
            messageEntity.from = PersonEntity(
                    identity = it.from?.identity,
                    firstName = it.from?.firstName,
                    lastName = it.from?.lastName,
                    profileImage = it.from?.profileImage
            )
            messageEntity.to = PersonEntity(
                    identity = it.to?.identity,
                    firstName = it.to?.firstName,
                    lastName = it.to?.lastName,
                    profileImage = it.to?.profileImage
            )
            // Create At
            it.createAt?.let {
                messageEntity.createAt = it.ToDateTime(appContext
                        .getString(R.string.date_time_format))
            }

            return messageEntity

        } ?: MessageEntity()

    }


    /**
     * Params
     */
    data class Params(

            /**
             * Conversation
             */
            var conversation: String,

            /**
             * Text
             */
            var text: String,

            /**
             * From
             */
            var from: String,

            /**
             * To
             */
            var to: String
    )


}