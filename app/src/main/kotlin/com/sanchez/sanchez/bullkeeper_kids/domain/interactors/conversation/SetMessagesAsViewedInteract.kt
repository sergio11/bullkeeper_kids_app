package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation

import com.sanchez.sanchez.bullkeeper_kids.core.extension.empty
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
import retrofit2.Retrofit
import java.io.Serializable
import javax.inject.Inject

/**
 * Set Messages As Viewed Interact
 */
class SetMessagesAsViewedInteract
    @Inject constructor(
            private val conversationService: IConversationService,
            retrofit: Retrofit): UseCase<String, SetMessagesAsViewedInteract.Params>(retrofit) {

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): String  =
            conversationService.setMessagesAsViewed(params.id, params.messageIds).await().data ?: String.empty()


    /**
     * Params
     */
    data class Params(val id: String, val messageIds: List<String> = ArrayList()): Serializable
}