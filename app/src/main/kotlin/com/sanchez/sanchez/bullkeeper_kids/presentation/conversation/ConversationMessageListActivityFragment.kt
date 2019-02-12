package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation

import android.content.Context
import android.os.Bundle
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ConversationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import java.lang.IllegalArgumentException
import javax.inject.Inject

/**
 * Conversation Message List Activity Fragment
 */
class ConversationMessageListActivityFragment : BaseFragment() {

    /**
     * Activity Handler
     */
    private lateinit var activityHandler: IConversationMessageListHandler

    /**
     * Context
     */
    @Inject
    internal lateinit var context: Context

    /**
     * Sound Manager
     */
    @Inject
    internal lateinit var soundManager: ISoundManager

    /**
     * Conversation Message List View Model
     */
    @Inject
    internal lateinit var conversationMessageListViewModel: ConversationMessageListViewModel

    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context !is IConversationMessageListHandler)
            throw IllegalArgumentException("Context must implement IConversation Message List Handler")

        activityHandler = context
    }

    /**
     * Layout Id
     */
    override fun layoutId(): Int = R.layout.fragment_conversation_message_list


    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInjector()
    }


    /**
     * Initialize Injector
     */
    fun initializeInjector() {
        val conversationComponent = ConversationComponent::class.java
                .cast((activity as HasComponent<*>)
                        .component)
        conversationComponent?.inject(this)
    }
}
