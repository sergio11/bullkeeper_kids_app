package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation

import com.sanchez.sanchez.bullkeeper_kids.core.extension.empty
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Delete Conversation Messages Interact
 */
class DeleteConversationMessagesInteract
    @Inject constructor(
            private val conversationService: IConversationService,
            retrofit: Retrofit): UseCase<String,
            DeleteConversationMessagesInteract.Params>(retrofit){

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): String {

        // Delete Conversation Messages
        val response = conversationService.deleteConversationMessage(
                id = params.id,
                messages = params.ids
        ).await()

        return response.data ?: String.empty()

    }


    /**
     * Params
     */
    data class Params(

            /**
             * Id
             */
            var id: String,

            /**
             * Ids
             */
            var ids: List<String>
    )


}