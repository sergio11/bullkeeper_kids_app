package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ConversationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.DaggerConversationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
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
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

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
     *
    */
    override fun onResume() {
        super.onResume()
        preferenceRepository.setConversationMessageOverlayNotificationEnabled(false)
    }

    /**
     *
     */
    override fun onStop() {
        super.onStop()
        preferenceRepository.setConversationMessageOverlayNotificationEnabled(true)
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


    /**
     * On Connectivity Not Available
     */
    override fun onConnectivityNotAvailable() {
        super.onConnectivityNotAvailable()

        showNoticeDialog(R.string.connectivity_not_available, object : NoticeDialogFragment.NoticeDialogListener {
            override fun onAccepted(dialog: DialogFragment) {
                navigator.showHome(this@ConversationMessageListActivity)
            }
        })
    }



    companion object {


        /**
         * Events
         */
        const val MESSAGE_SAVED_EVENT = "com.sanchez.sanchez.sergio.bullkeeper.message.saved"
        const val ALL_CONVERSATION_DELETED_EVENT = "com.sanchez.sanchez.sergio.bullkeeper.all.conversation.deleted"
        const val ALL_MESSAGES_DELETED_EVENT = "com.sanchez.sanchez.sergio.bullkeeper.all.messages.deleted"
        const val DELETED_MESSAGES_EVENT = "com.sanchez.sanchez.sergio.bullkeeper.deleted_messages"
        const val SET_MESSAGES_AS_VIEWED_EVENT = "com.sanchez.sanchez.sergio.bullkeeper.messages.viewed"
        const val DELETE_CONVERSATION_EVENT = "com.sanchez.sanchez.sergio.bullkeeper.delete.conversation"
        /**
         * Args
         */
        const val CONVERSATION_ID_ARG = "CONVERSATION_ID_ARG"
        const val MEMBER_ONE_ARG = "MEMBER_ONE_ARG"
        const val MEMBER_TWO_ARG = "MEMBER_TWO_ARG"
        const val MESSAGE_SAVED_ARG = "MESSAGE_SAVED_ARG"
        const val ALL_CONVERSATION_DELETED_ARG = "ALL_CONVERSATION_DELETED_ARG"
        const val ALL_MESSAGES_DELETED_ARG = "ALL_MESSAGES_DELETED_ARG"
        const val DELETED_MESSAGES_ARG = "DELETED_MESSAGES_ARG"
        const val SET_MESSAGES_AS_VIEWED_ARG = "SET_MESSAGES_AS_VIEWED_ARG"

        /**
         * Calling Intent
         */
        @JvmStatic
        fun callingIntent(context: Context, conversation: String): Intent
                = Intent(context, ConversationMessageListActivity::class.java).apply {
            putExtra(CONVERSATION_ID_ARG, conversation)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }


        /**
         * Calling Intent
         */
        @JvmStatic
        fun callingIntent(context: Context, memberOne: String, memberTwo: String): Intent
                = Intent(context, ConversationMessageListActivity::class.java).apply {
            putExtra(MEMBER_ONE_ARG, memberOne)
            putExtra(MEMBER_TWO_ARG, memberTwo)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        /**
         * Calling Intent
         */
        @JvmStatic
        fun callingIntent(activity: Activity, conversation: String): Intent
            = Intent(activity, ConversationMessageListActivity::class.java).apply {
            putExtra(CONVERSATION_ID_ARG, conversation)
        }


        /**
         * Calling Intent
         */
        @JvmStatic
        fun callingIntent(activity: Activity, memberOne: String, memberTwo: String): Intent
                = Intent(activity, ConversationMessageListActivity::class.java).apply {
            putExtra(MEMBER_ONE_ARG, memberOne)
            putExtra(MEMBER_TWO_ARG, memberTwo)
        }
    }

}
