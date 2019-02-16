package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.list

import com.sanchez.sanchez.bullkeeper_kids.core.platform.IBasicActivityHandler

/**
 * Conversation List Handler
 */
interface IConversationListHandler: IBasicActivityHandler {

    /**
     * Show Conversation Message List
     */
    fun showConversationMessageList(conversation: String)

}