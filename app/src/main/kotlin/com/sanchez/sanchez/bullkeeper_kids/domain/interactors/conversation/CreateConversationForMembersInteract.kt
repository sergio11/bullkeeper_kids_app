package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.extension.ToDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
import com.sanchez.sanchez.bullkeeper_kids.domain.models.ConversationEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.PersonEntity
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Create Conversation For Members Interact
 */
class CreateConversationForMembersInteract
    @Inject constructor(
            private val appContext: Context,
            private val conversationService: IConversationService,
            retrofit: Retrofit
    ) : UseCase<ConversationEntity, CreateConversationForMembersInteract.Params>(retrofit){


    /**
     *
     */
    override suspend fun onExecuted(params: CreateConversationForMembersInteract.Params): ConversationEntity =
            conversationService.createConversation(params.memberOne, params.memberTwo).await().data?.let {
                ConversationEntity().apply {
                    identity = it.identity
                    messagesCount = it.messagesCount
                    createAt = it.createAt?.ToDateTime(
                            appContext.getString(R.string.date_format_server_response)
                    )
                    updateAt = it.updateAt?.ToDateTime(
                            appContext.getString(R.string.date_format_server_response)
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

                }
            } ?: ConversationEntity()

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
}