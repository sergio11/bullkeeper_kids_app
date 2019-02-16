package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation

import com.sanchez.sanchez.bullkeeper_kids.core.extension.empty
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Delete Conversation Messages For Members Interact
 */
class DeleteConversationMessagesForMembersInteract
    @Inject constructor(
            private val conversationService: IConversationService,
            retrofit: Retrofit): UseCase<String,
            DeleteConversationMessagesForMembersInteract.Params>(retrofit){

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): String {

        // Delete Conversation Messages
        val response = conversationService
                .deleteConversationForMembers(params.memberOne, params.memberTwo, params.ids).await()

        return response.data ?: String.empty()

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
            var memberTwo: String,

            /**
             * Ids
             */
            var ids: List<String>
    )


}