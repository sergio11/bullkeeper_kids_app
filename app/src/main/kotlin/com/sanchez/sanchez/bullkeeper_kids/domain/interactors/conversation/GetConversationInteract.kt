package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.extension.ToDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
import com.sanchez.sanchez.bullkeeper_kids.domain.models.*
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Get Conversation Interact
 */
class GetConversationInteract
    @Inject constructor(
        private val context: Context,
        private val conversationService: IConversationService,
        retrofit: Retrofit): UseCase<ConversationEntity, GetConversationInteract.Params>(retrofit){

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): ConversationEntity {

        val response =
                conversationService.getConversationById(params.id).await()

        return response.data?.let {

            val conversationEntity = ConversationEntity()
            conversationEntity.identity = it.identity
            conversationEntity.messagesCount = it.messagesCount

            it.createAt?.let {
                conversationEntity.createAt = it.ToDateTime(
                        context.getString(R.string.date_time_format)
                )
            }

            it.updateAt?.let {
                conversationEntity.updateAt = it.ToDateTime(
                        context.getString(R.string.date_time_format)
                )
            }

            conversationEntity.memberOne = PersonEntity().apply {
                identity = it.memberOne?.identity
                firstName = it.memberOne?.firstName
                lastName = it.memberOne?.lastName
                profileImage = it.memberOne?.profileImage
            }

            conversationEntity.memberTwo = PersonEntity().apply {
                identity = it.memberTwo?.identity
                firstName = it.memberTwo?.firstName
                lastName = it.memberTwo?.lastName
                profileImage = it.memberTwo?.profileImage
            }

            return conversationEntity

        } ?: ConversationEntity()
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


}