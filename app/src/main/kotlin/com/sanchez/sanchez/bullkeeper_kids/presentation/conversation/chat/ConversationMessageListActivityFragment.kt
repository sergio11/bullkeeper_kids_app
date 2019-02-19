package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.chat

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ConversationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.extension.empty
import com.sanchez.sanchez.bullkeeper_kids.core.extension.invisible
import com.sanchez.sanchez.bullkeeper_kids.core.extension.visible
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.ConfirmationDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.commons.models.MessageContentType
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.stfalcon.chatkit.utils.DateFormatter
import kotlinx.android.synthetic.main.fragment_conversation_message_list.*
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Conversation Message List Activity Fragment
 */
class ConversationMessageListActivityFragment : BaseFragment(),
        MessagesListAdapter.SelectionListener, MessagesListAdapter.OnLoadMoreListener,
        DateFormatter.Formatter, MessageInput.InputListener {


    private val SENDING_MESSAGE_ID = "5645678"

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
    internal lateinit var viewModel: ConversationMessageListViewModel

    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * Image Loader
     */
    @Inject
    internal lateinit var imageLoader: ImageLoader

    /**
     * Conversation
     */
    private var conversation: String? = null

    /**
     * Member One
     */
    private var memberOne: String? = null

    /**
     * Member Two
     */
    private var memberTwo: String? = null

    /**
     * Selection Count
     */
    private var selectionCount: Int = 0

    /**
     * Current User Id
     */
    private val currentUserId: String by lazy {
        preferenceRepository.getPrefKidIdentity()
    }

    /**
     * Target User
     */
    private var targetUser: String? = null

    /**
     * Message Adapter
     */
    private lateinit var messagesAdapter: MessagesListAdapter<ConversationMessage>

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

        // Init View Model
        viewModel.init()

        if(arguments?.isEmpty == true)
            throw IllegalStateException("Arguments can not be null")

        conversation = arguments?.get(ConversationMessageListActivity.CONVERSATION_ID_ARG) as String?
        memberOne = arguments?.get(ConversationMessageListActivity.MEMBER_ONE_ARG) as String?
        memberTwo = arguments?.get(ConversationMessageListActivity.MEMBER_TWO_ARG) as String?

        // init message adapter
        messagesAdapter = MessagesListAdapter(preferenceRepository.getPrefKidIdentity(),
                imageLoader)
        messagesAdapter.enableSelectionMode(this)
        messagesAdapter.setLoadMoreListener(this)
        messagesAdapter.setDateHeadersFormatter(this)
        messagesList.setAdapter(messagesAdapter)

        clearMessage.setOnClickListener {

            if(selectionCount > 0) {

                activityHandler.showConfirmationDialog(R.string.delete_conversation_selected_messages_confirm, object: ConfirmationDialogFragment.ConfirmationDialogListener{

                    override fun onAccepted(dialog: DialogFragment) {
                        val conversationMessagesSelected = messagesAdapter.selectedMessages
                                .map { it.messageId }.toList()
                        conversation?.let { viewModel.deleteAllMessages(it, conversationMessagesSelected) } ?:
                        viewModel.deleteAllMessages(memberOne!!, memberTwo!!, conversationMessagesSelected)
                    }

                    override fun onRejected(dialog: DialogFragment) {}
                })


            } else {

                activityHandler.showConfirmationDialog(R.string.delete_all_conversation_messages_confirm, object: ConfirmationDialogFragment.ConfirmationDialogListener{

                    override fun onAccepted(dialog: DialogFragment) {
                        conversation?.let { viewModel.deleteAllMessages(it) } ?:
                        viewModel.deleteAllMessages(memberOne!!, memberTwo!!)
                    }

                    override fun onRejected(dialog: DialogFragment) {}
                })
            }


        }

        input.setInputListener(this)

        operationResultObserverHandler()


    }

    /**
     * On Start
     */
    override fun onStart() {
        super.onStart()
        if(!conversation.isNullOrEmpty()) {
            viewModel.loadConversationDetail(conversation!!)
        } else {
            viewModel.loadConversationDetail(memberOne!!, memberTwo!!)
        }
    }

    /**
     * On Back Pressed
     */
    override fun onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed()
        } else {
            messagesAdapter.unselectAllItems()
        }
    }

    /**
     * Format
     */
    override fun format(date: Date?): String = String.empty()

    /**
     * On Load More
     */
    override fun onLoadMore(page: Int, totalItemsCount: Int) {}

    /**
     * On Selection Changed
     */
    override fun onSelectionChanged(count: Int) {
        selectionCount = count
    }

    /**
     * On Submit
     */
    override fun onSubmit(text: CharSequence?): Boolean {
        input.isEnabled = false
        input.isSaveEnabled = false
        messagesAdapter.addToStart(getSendingConversationMessage(), true)
        viewModel.addMessage(conversation!!, text.toString(),
                currentUserId, targetUser!!)
        return true
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


    /**
     * Get Sending Conversation Message
     * @return
     */
    private fun getSendingConversationMessage(): ConversationMessage {
        return ConversationMessage(
                messageId = SENDING_MESSAGE_ID,
                messageUser = ConversationMessageUser(
                        currentUserId, "Sergio",
                        "https://avatars3.githubusercontent.com/u/6996211?s=460&v=4", true),
                messageText = getString(R.string.sending_message_wait),
                messageCreatedAt = Date())
    }



    /**
     * Operation Result Observer Handler
     */
    private fun operationResultObserverHandler(){
        viewModel.result.observe(this, Observer<ConversationMessageListViewModel.OperationResultEnum> { operationResult ->
            when(operationResult) {
                ConversationMessageListViewModel.OperationResultEnum.LOADING -> loadingData()
                ConversationMessageListViewModel.OperationResultEnum.CONVERSATION_DETAIL_FAILED -> onConversationDetailFailed()
                ConversationMessageListViewModel.OperationResultEnum.CONVERSATION_DETAIL_LOADED -> onConversationDetailLoaded()
                ConversationMessageListViewModel.OperationResultEnum.NO_MESSAGES_FOUND -> onNoMessagesFound()
                ConversationMessageListViewModel.OperationResultEnum.MESSAGES_FOUND -> onMessagesFound()
                ConversationMessageListViewModel.OperationResultEnum.MESSAGE_ADDED_SUCCESSFULLY -> onMessageAddedSuccessfully()
                ConversationMessageListViewModel.OperationResultEnum.CREATE_MESSAGE_FAILED -> onCreateMessageFailed()
                ConversationMessageListViewModel.OperationResultEnum.MESSAGES_DELETED_SUCCESSFULLY -> onMessagesDeletedSuccessfully()
                ConversationMessageListViewModel.OperationResultEnum.MESSAGES_SELECTED_DELETED_SUCCESSFULLY -> onMessagesSelectedDeletedSuccessfully()
                ConversationMessageListViewModel.OperationResultEnum.DELETE_MESSAGES_FAILED -> onDeleteMessageFailed()
                ConversationMessageListViewModel.OperationResultEnum.LOAD_MESSAGES_ERROR -> onLoadMessageError()
                else -> { }
            }
        })
    }


    /**
     * On Conversation Detail Failed
     */
    private fun onConversationDetailFailed(){
        activityHandler.hideProgressDialog()
        activityHandler.showNoticeDialog(R.string.load_conversation_error, object: NoticeDialogFragment.NoticeDialogListener{
            override fun onAccepted(dialog: DialogFragment) {
                activityHandler.closeActivity()
            }
        })
    }

    /**
     * On Conversation Detail Loaded
     */
    private fun onConversationDetailLoaded(){
        activityHandler.hideProgressDialog()

        viewModel.conversationDetail.value?.let {
            conversation = it.identity ?: String.empty()
            memberOne = it.memberOne?.identity ?: String.empty()
            memberTwo = it.memberTwo?.identity ?: String.empty()

            targetUser = if(memberOne.equals(currentUserId))
                memberTwo else memberOne

            viewModel.loadMessages(conversation!!)
        }

        content.setOnRefreshListener {
            viewModel.loadMessages(conversation!!)
        }
    }

    /**
     * On No Messages Found
     */
    private fun onNoMessagesFound(){
        activityHandler.hideProgressDialog()
        clearMessage.invisible()

    }

    /**
     * On Messages Found
     */
    private fun onMessagesFound(){
        activityHandler.hideProgressDialog()

        content.isRefreshing = false
        clearMessage.visible()

        // Set Messages as viewed
        val messagesViewed = viewModel.messages.value?.filter {
            it.to?.identity == currentUserId && !it.viewed && !it.identity.isNullOrEmpty() }?.map { it.identity!! }
                ?.toList() ?: ArrayList()

        if(messagesViewed.isNotEmpty())
            viewModel.setMessagesAsViewed(conversation!!, messagesViewed)


        messagesAdapter.clear(true)
        messagesAdapter.addToEnd(viewModel.messages.value?.map {
            ConversationMessage(
                    messageId = it.identity ?: String.empty(),
                    messageText = it.text ?: String.empty(),
                    messageCreatedAt = it.createAt ?: Date(),
                    messageUser = ConversationMessageUser(
                            userId = it.from?.identity ?: String.empty(),
                            userName = String.format(Locale.getDefault(), "%s %s",
                                    it.from?.firstName ?: String.empty(), it.from?.lastName ?: String.empty()),
                            userAvatar = it.from?.profileImage ?: String.empty(),
                            online = true))

        } ?: ArrayList(), false)

    }

    /**
     * On Message Added Successfully
     */
    private fun onMessageAddedSuccessfully(){
        activityHandler.hideProgressDialog()

        viewModel.messages.value?.let {

            val lastMessageAdded = it[it.size - 1]
            soundManager.playSound(R.raw.send_message_sound)
            clearMessage.visible()
            messagesAdapter.deleteById(SENDING_MESSAGE_ID)
            messagesAdapter.addToStart(
                    ConversationMessage(
                            messageId = lastMessageAdded.identity ?: String.empty(),
                            messageText = lastMessageAdded.text ?: String.empty(),
                            messageCreatedAt = lastMessageAdded.createAt ?: Date(),
                            messageUser = ConversationMessageUser(
                                    userId = lastMessageAdded.from?.identity ?: String.empty(),
                                    userName = String.format(Locale.getDefault(), "%s %s",
                                            lastMessageAdded.from?.firstName ?: String.empty(), lastMessageAdded.from?.lastName ?: String.empty()),
                                    userAvatar = lastMessageAdded.from?.profileImage ?: String.empty(),
                                    online = true)), true)

        }
    }

    /**
     * On Messages Deleted Successfully
     */
    private fun onMessagesDeletedSuccessfully(){
        activityHandler.hideProgressDialog()
        if(viewModel.messages.value?.isNotEmpty() == true)
            clearMessage.visible()
        else
            clearMessage.invisible()
        messagesAdapter.clear(true)
        activityHandler.showNoticeDialog(R.string.all_messages_deleted)

    }

    /**
     * On Messages Selected Deleted Successfully
     */
    private fun onMessagesSelectedDeletedSuccessfully(){
        activityHandler.hideProgressDialog()
        if(viewModel.messages.value?.isNotEmpty() == true)
            clearMessage.visible()
        else
            clearMessage.invisible()
        messagesAdapter.deleteSelectedMessages()
        activityHandler.showNoticeDialog(R.string.selected_messages_deleted)
    }

    /**
     * On Delete Message Failed
     */
    private fun onDeleteMessageFailed(){
        activityHandler.hideProgressDialog()
        messagesAdapter.unselectAllItems()
        activityHandler.showNoticeDialog(R.string.error_deleting_messages)
    }

    /**
     * On Load Message Error
     */
    private fun onLoadMessageError(){
        activityHandler.hideProgressDialog()

        activityHandler.showNoticeDialog(R.string.load_conversation_error, object: NoticeDialogFragment.NoticeDialogListener{
            override fun onAccepted(dialog: DialogFragment) {
                activityHandler.closeActivity()
            }
        })
    }

    /**
     * On Create Message Failed
     */
    private fun onCreateMessageFailed(){
        activityHandler.hideProgressDialog()
        messagesAdapter.deleteById(SENDING_MESSAGE_ID)
        activityHandler.showNoticeDialog(R.string.error_create_message)
        if(viewModel.messages.value?.isNotEmpty() == true)
            clearMessage.visible()
        else
            clearMessage.invisible()
    }

    /**
     * Loading Data
     */
    private fun loadingData() {
        activityHandler.showProgressDialog(R.string.generic_loading_text)
    }

    /**
     * Conversation Message
     */
    data class ConversationMessage(
            var messageId: String,
            var messageText: String,
            var messageCreatedAt: Date = Date(),
            var messageUser: ConversationMessageUser,
            var image: Image? = null,
            var voice: Voice? = null
    ): IMessage, MessageContentType.Image, MessageContentType {

        /**
         * Get Message Id
         */
        override fun getId() = messageId

        /**
         * Get Message Created At
         */
        override fun getCreatedAt(): Date = messageCreatedAt

        /**
         * Get Conversation Message User
         */
        override fun getUser() = messageUser

        /**
         * Get Message Text
         */
        override fun getText() = messageText

        /**
         * Get Image Url
         */
        override fun getImageUrl() = image?.url


        class Image(var url: String)

        class Voice(val url: String, val duration: Int)

    }

    /**
     * Conversation Message User
     */
    data class ConversationMessageUser(
            var userId: String,
            var userName: String,
            var userAvatar: String,
            var online: Boolean
    ): IUser {

        /**
         * Get Avatar
         */
        override fun getAvatar() = userAvatar

        /**
         * Get Name
         */
        override fun getName() = userName

        /**
         * Get Id
         */
        override fun getId() = userId
    }
}
