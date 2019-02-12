package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation

import com.sanchez.sanchez.bullkeeper_kids.core.extension.empty
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Delete Conversation Interact
 */
class DeleteConversationInteract
    @Inject constructor(
        private val conversationService: IConversationService,
        retrofit: Retrofit): UseCase<String, DeleteConversationInteract.Params>(retrofit){

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): String {

        // Delete Conversation By Id
        val response =
                conversationService.deleteConversationById(params.id).await()

        return response.data ?: String.empty()

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