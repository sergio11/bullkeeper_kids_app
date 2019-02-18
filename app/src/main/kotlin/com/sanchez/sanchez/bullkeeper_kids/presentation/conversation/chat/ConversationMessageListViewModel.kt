package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.chat

import android.arch.lifecycle.MutableLiveData
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation.*
import com.sanchez.sanchez.bullkeeper_kids.domain.models.ConversationEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.MessageEntity
import javax.inject.Inject

/**
 * Conversation Message List View Model
 */
class ConversationMessageListViewModel
    @Inject constructor(
            private val addMessageInteract: AddConversationMessageInteract,
            private val getConversationMessagesInteract: GetConversationMessagesInteract,
            private val deleteConversationMessagesInteract: DeleteConversationMessagesInteract,
            private val getConversationMessagesForMembersInteract: GetConversationMessagesForMembersInteract,
            private val deleteConversationMessagesForMembersInteract: DeleteConversationMessagesForMembersInteract,
            private val getConversationInteract: GetConversationInteract,
            private val getConversationForMembersInteract: GetConversationForMembersInteract,
            private val createConversationForMembersInteract: CreateConversationForMembersInteract
    )
    : BaseViewModel()  {

    /**
     * Operation Result Enum
     */
    enum class OperationResultEnum {
        NO_OPERATION_RESULT,
        LOADING,
        MESSAGES_DELETED_SUCCESSFULLY,
        MESSAGES_SELECTED_DELETED_SUCCESSFULLY,
        DELETE_MESSAGES_FAILED,
        MESSAGE_ADDED_SUCCESSFULLY,
        CREATE_MESSAGE_FAILED,
        MESSAGES_FOUND,
        NO_MESSAGES_FOUND,
        LOAD_MESSAGES_ERROR,
        CONVERSATION_DETAIL_LOADED,
        CONVERSATION_DETAIL_FAILED
    }

    /**
     * Conversation
     */
    val conversationDetail: MutableLiveData<ConversationEntity> by lazy {
        MutableLiveData<ConversationEntity>()
    }

    /**
     * Messages
     */
    val messages: MutableLiveData<ArrayList<MessageEntity>> by lazy {
        MutableLiveData<ArrayList<MessageEntity>>()
    }

    /**
     * Result
     */
    val result: MutableLiveData<OperationResultEnum> by lazy {
        MutableLiveData<OperationResultEnum>()
    }

    /**
     * Init
     */
    override fun init() {
        messages.value = ArrayList()
        result.value = OperationResultEnum.NO_OPERATION_RESULT
    }

    /**
     * Load Conversation Detail
     * @param conversation
     */
    fun loadConversationDetail(conversation: String){
        result.value = OperationResultEnum.LOADING
        getConversationInteract(GetConversationInteract.Params(conversation)){
            it.either(fnR = fun(conversation: ConversationEntity) {
                conversationDetail.value = conversation
                result.postValue(OperationResultEnum.CONVERSATION_DETAIL_LOADED)
            }, fnL = fun(failure: Failure){
                when(failure) {
                    is GetConversationInteract.ConversationNotFoundFailure -> null
                    else -> result.postValue(OperationResultEnum.CONVERSATION_DETAIL_FAILED)
                }

            })
        }
    }

    /**
     * Load Conversation Detail
     * @param memberOne
     * @param memberTwo
     */
    fun loadConversationDetail(memberOne: String, memberTwo: String) {
        result.value = OperationResultEnum.LOADING
        getConversationForMembersInteract(GetConversationForMembersInteract.Params(memberOne, memberTwo)){
            it.either(fnR = fun(conversation: ConversationEntity) {
                conversationDetail.value = conversation
                result.postValue(OperationResultEnum.CONVERSATION_DETAIL_LOADED)
            }, fnL = fun(failure: Failure){
                when(failure) {
                    is GetConversationForMembersInteract.ConversationNotFoundFailure -> createConversation(memberOne, memberTwo)
                    else -> result.postValue(OperationResultEnum.CONVERSATION_DETAIL_FAILED)
                }
            })
        }
    }

    /**
     * Create Conversation
     * @param memberOne
     * @param memberTwo
     */
    fun createConversation(memberOne: String, memberTwo: String) {
        result.value = OperationResultEnum.LOADING
        createConversationForMembersInteract(CreateConversationForMembersInteract.Params(memberOne, memberTwo)) {
            it.either(fnR = fun(conversation: ConversationEntity) {
                conversationDetail.value = conversation
                result.postValue(OperationResultEnum.CONVERSATION_DETAIL_LOADED)
            }, fnL = fun(_: Failure){
                result.postValue(OperationResultEnum.CONVERSATION_DETAIL_FAILED)
            })
        }
    }

    /**
     * Load Messages
     * @param conversation
     */
    fun loadMessages(conversation: String) {
        result.value = OperationResultEnum.LOADING
        getConversationMessagesInteract(GetConversationMessagesInteract.Params(conversation)){
            it.either(fnR = fun(conversationMessages: List<MessageEntity>) {
                if(!conversationMessages.isNullOrEmpty()) {
                    messages.value = ArrayList(conversationMessages)
                    result.postValue(OperationResultEnum.MESSAGES_FOUND)
                } else {
                    result.postValue(OperationResultEnum.NO_MESSAGES_FOUND)
                }
            }, fnL = fun(failure: Failure){
                when(failure) {
                    is GetConversationMessagesInteract.NoMessagesFoundFailure -> result.postValue(OperationResultEnum.NO_MESSAGES_FOUND)
                    else ->  result.postValue(OperationResultEnum.LOAD_MESSAGES_ERROR)
                }
            })
        }
    }

    /**
     * Load Messages
     * @param memberOne
     * @param memberTwo
     */
    fun loadMessages(memberOne: String, memberTwo: String) {
        result.value = OperationResultEnum.LOADING
        getConversationMessagesForMembersInteract(GetConversationMessagesForMembersInteract
                .Params(
                        memberOne = memberOne,
                        memberTwo = memberTwo
                )){
            it.either(fnR = fun(conversationMessages: List<MessageEntity>) {
                if(!conversationMessages.isNullOrEmpty()) {
                    messages.value = ArrayList(conversationMessages)
                    result.postValue(OperationResultEnum.MESSAGES_FOUND)
                } else {
                    result.postValue(OperationResultEnum.NO_MESSAGES_FOUND)
                }
            }, fnL = fun(failure: Failure){
                when(failure) {
                    is GetConversationMessagesForMembersInteract.NoMessagesFoundFailure -> result.postValue(OperationResultEnum.NO_MESSAGES_FOUND)
                    else ->  result.postValue(OperationResultEnum.LOAD_MESSAGES_ERROR)
                }
            })
        }
    }

    /**
     * Delete All Messages
     * @param conversation
     * @param ids
     */
    fun deleteAllMessages(conversation: String, ids: List<String> = ArrayList()){
        result.value = OperationResultEnum.LOADING
        deleteConversationMessagesInteract(
                DeleteConversationMessagesInteract.Params(conversation, ids)
        ){
            it.either(fnR = fun(_: String) {
                if(ids.isEmpty()) {
                    result.value = OperationResultEnum.MESSAGES_DELETED_SUCCESSFULLY
                    messages.value = ArrayList()
                } else {
                    result.value = OperationResultEnum.MESSAGES_SELECTED_DELETED_SUCCESSFULLY
                    val messagesToRemove = messages.value?.filter { ids.contains(it.identity!!) }
                    messagesToRemove?.let { messages.value?.removeAll(it) }
                }

            }, fnL = fun(_: Failure){
                result.value = OperationResultEnum.DELETE_MESSAGES_FAILED
            })
        }
    }

    /**
     * Delete All Messages
     * @param memberOne
     * @param memberTwo
     * @param ids
     */
    fun deleteAllMessages(memberOne: String, memberTwo: String, ids: List<String> = ArrayList()){
        result.value = OperationResultEnum.LOADING
        deleteConversationMessagesForMembersInteract(
                DeleteConversationMessagesForMembersInteract.Params(memberOne, memberTwo, ids)
        ){
            it.either(fnR = fun(_: String) {
                if(ids.isEmpty()) {
                    result.value = OperationResultEnum.MESSAGES_DELETED_SUCCESSFULLY
                    messages.value = ArrayList()
                } else {
                    result.value = OperationResultEnum.MESSAGES_SELECTED_DELETED_SUCCESSFULLY
                    val messagesToRemove = messages.value?.filter { ids.contains(it.identity!!) }
                    messagesToRemove?.let { messages.value?.removeAll(it) }
                }

            }, fnL = fun(_: Failure){
                result.value = OperationResultEnum.DELETE_MESSAGES_FAILED
            })
        }
    }

    /**
     * Add Message
     * @param conversation
     * @param text
     * @param from
     * @param to
     */
    fun addMessage(conversation: String, text: String, from: String, to: String) {
        result.value = OperationResultEnum.LOADING
        addMessageInteract(
                AddConversationMessageInteract.Params(
                        conversation,
                        text,
                        from,
                        to
                )
        ){
            it.either(fnR = fun(message: MessageEntity) {
                messages.value?.add(message)
                result.value = OperationResultEnum.MESSAGE_ADDED_SUCCESSFULLY
            }, fnL = fun(_: Failure){
                result.value = OperationResultEnum.CREATE_MESSAGE_FAILED
            })
        }
    }

}