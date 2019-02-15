package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation

import com.sanchez.sanchez.bullkeeper_kids.core.extension.empty
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Delete All Conversation For Member Interact
 */
class DeleteAllConversationForMemberInteract
    @Inject constructor(
        private val conversationService: IConversationService,
        retrofit: Retrofit): UseCase<String, DeleteAllConversationForMemberInteract.Params>(retrofit){

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): String {

        // Delete Conversations for member
        val response =
                conversationService.deleteConversationsForMember(params.memberId).await()

        return response.data ?: String.empty()

    }


    /**
     * Params
     */
    data class Params(

            /**
             * Member Id
             */
            var memberId: String
    )


}