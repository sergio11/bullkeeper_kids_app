package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation

import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation.AddConversationMessageInteract
import javax.inject.Inject

/**
 * Conversation Message List View Model
 */
class ConversationMessageListViewModel
    @Inject constructor(
            private val addMessageInteract: AddConversationMessageInteract
    )
    : BaseViewModel()  {



}