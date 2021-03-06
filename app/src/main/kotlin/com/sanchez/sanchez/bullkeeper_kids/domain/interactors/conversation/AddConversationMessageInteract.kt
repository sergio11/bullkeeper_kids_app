package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.extension.ToDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddMessageDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
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
            private val apiEndPointsHelper: ApiEndPointsHelper,
            private val conversationService: IConversationService,
            retrofit: Retrofit): UseCase<MessageEntity, AddConversationMessageInteract.Params>(retrofit){

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params): MessageEntity  =
            conversationService.addMessage(params.conversation,
                    AddMessageDTO(params.conversation, params.text, params.from, params.to)).await().data.let {
                MessageEntity().apply {
                    identity = it?.identity
                    viewed = it?.viewed ?: false
                    text = it?.text
                    conversation = it?.conversation
                    from = PersonEntity(
                            identity = it?.from?.identity,
                            firstName = it?.from?.firstName,
                            lastName = it?.from?.lastName,
                            profileImage = it?.from?.profileImage?.let {
                                apiEndPointsHelper.getProfileUrl(it)
                            }
                    )
                    to = PersonEntity(
                            identity = it?.to?.identity,
                            firstName = it?.to?.firstName,
                            lastName = it?.to?.lastName,
                            profileImage = it?.to?.profileImage?.let {
                                apiEndPointsHelper.getProfileUrl(it)
                            }
                    )
                    createAt = it?.createAt?.ToDateTime(appContext
                            .getString(R.string.date_time_format_2))

                }
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