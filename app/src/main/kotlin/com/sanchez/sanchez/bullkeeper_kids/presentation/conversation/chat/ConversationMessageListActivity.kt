package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ConversationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.DaggerConversationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import kotlinx.android.synthetic.main.app_translucent_toolbar_return.*
import javax.inject.Inject

/**
 * Conversation Message List Activity
 */
class ConversationMessageListActivity : BaseActivity(),
        HasComponent<ConversationComponent>, IConversationMessageListHandler {


    val TAG = "CONVERSATION_MESSAGE_LIST"


    /**
     * Conversation Component
     */
    private val conversationComponent: ConversationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerConversationComponent
                .builder()
                .applicationComponent((application as AndroidApplication).appComponent)
                .activityModule(ActivityModule(this))
                .build()
    }


    /**
     * App Component
     */
    override val component: ConversationComponent
        get() = conversationComponent


    /**
     * Dependencies
     * =================
     */

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: INavigator



    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        conversationComponent.inject(this)

        backIcon.setOnClickListener {
            onBackPressed()
        }

        appTitle.setOnClickListener {
            navigator.showHome(this)
        }

    }

    /**
     * Get Layout Resource
     */
    override fun getLayoutRes(): Int = R.layout.activity_conversation_message_list


    /**
     * Fragment
     */
    override fun fragment(): BaseFragment = ConversationMessageListActivityFragment().apply {
        arguments = intent.extras
    }


    companion object {

        /**
         * Args
         */
        const val CONVERSATION_ID_ARG = "CONVERSATION_ID_ARG"
        const val MEMBER_ONE_ARG = "MEMBER_ONE_ARG"
        const val MEMBER_TWO_ARG = "MEMBER_TWO_ARG"


        /**
         * Calling Intent
         */
        @JvmStatic
        fun callingIntent(context: Context, conversation: String): Intent
            = Intent(context, ConversationMessageListActivity::class.java).apply {
            putExtra(CONVERSATION_ID_ARG, conversation)
        }


        /**
         * Calling Intent
         */
        @JvmStatic
        fun callingIntent(context: Context, memberOne: String, memberTwo: String): Intent
                = Intent(context, ConversationMessageListActivity::class.java).apply {
            putExtra(MEMBER_ONE_ARG, memberOne)
            putExtra(MEMBER_TWO_ARG, memberTwo)
        }
    }

}
