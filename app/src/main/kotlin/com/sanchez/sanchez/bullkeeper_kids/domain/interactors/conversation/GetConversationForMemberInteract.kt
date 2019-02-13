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
 * Get Conversation For Member Interact
 */
class GetConversationForMemberInteract
    @Inject constructor(
        private val context: Context,
        private val conversationService: IConversationService,
        retrofit: Retrofit): UseCase<List<ConversationEntity>, GetConversationForMemberInteract.Params>(retrofit){

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): List<ConversationEntity>  =
        conversationService.getConversationsForMember(params.id).await().data?.map { ConversationEntity().apply {
            identity = it.identity
            messagesCount = it.messagesCount
            createAt = it.createAt?.ToDateTime(context.getString(R.string.date_time_format))
            updateAt = it.updateAt?.ToDateTime(context.getString(R.string.date_time_format))
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
        } } ?: ArrayList()


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