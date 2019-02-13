package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.list

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
import javax.inject.Inject

/**
 * Conversation List Activity
 */
class ConversationListActivity : BaseActivity(),
        HasComponent<ConversationComponent>, IConversationListHandler {


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
    }

    /**
     * Get Layout Resource
     */
    override fun getLayoutRes(): Int = R.layout.activity_conversation_list


    /**
     * Fragment
     */
    override fun fragment(): BaseFragment = ConversationListActivityFragment()


    companion object {

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context): Intent
                = Intent(context, ConversationListActivity::class.java)
    }

}
