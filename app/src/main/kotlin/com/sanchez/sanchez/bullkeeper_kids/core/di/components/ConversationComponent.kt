package com.sanchez.sanchez.bullkeeper_kids.core.di.components


import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ConversationModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.chat.ConversationMessageListActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.chat.ConversationMessageListActivityFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.list.ConversationListActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.list.ConversationListActivityFragment
import dagger.Component

/**
 * Conversation Component
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class],
        modules = [ActivityModule::class, ConversationModule::class])
interface ConversationComponent: ActivityComponent {

    /**
     * Inject into Conversation Message List
     */
    fun inject(conversationMessageListActivity: ConversationMessageListActivity)

    /**
     * Inject into Conversation Message List Activity Fragment
     */
    fun inject(conversationMessageListActivityFragment: ConversationMessageListActivityFragment)

    /**
     * Inject into Conversation List Activity Fragment
     */
    fun inject(conversationListActivityFragment: ConversationListActivityFragment)

    /**
     * Inject into Conversation List Activity
     */
    fun inject(conversationListActivity: ConversationListActivity)

}