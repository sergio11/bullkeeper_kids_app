package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.list

import android.arch.lifecycle.MutableLiveData
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation.DeleteAllConversationForMemberInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation.DeleteConversationInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation.GetConversationForMemberInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.ConversationEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.LoadingStateEnum
import javax.inject.Inject

/**
 * Conversation List View Model
 */
class ConversationListViewModel
    @Inject constructor(
            private val getConversationForMemberInteract: GetConversationForMemberInteract,
            private val deleteConversationInteract: DeleteConversationInteract,
            private val deleteAllConversationForMemberInteract: DeleteAllConversationForMemberInteract
    )
    : BaseViewModel()  {

    /**
     * Operation Result Enum
     */
    enum class OperationResultEnum {
        CONVERSATION_DELETED_SUCCESSFULLY,
        NO_OPERATION_RESULT,
        DELETE_CONVERSATION_FAILED,
        DELETE_ALL_CONVERSATION_FAILED,
        ALL_CONVERSATION_DELETED_SUCCESSFULLY
    }

    /**
     * Conversations
     */
    val conversations: MutableLiveData<List<ConversationEntity>> by lazy {
        MutableLiveData<List<ConversationEntity>>()
    }


    /**
     * State
     */
    val state: MutableLiveData<LoadingStateEnum> by lazy {
        MutableLiveData<LoadingStateEnum>()
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
        state.value = LoadingStateEnum.LOADING
        conversations.value = ArrayList()
        result.value = OperationResultEnum.NO_OPERATION_RESULT
    }

    /**
     * Load Conversation
     */
    fun load(member: String){
        result.value = OperationResultEnum.NO_OPERATION_RESULT
        state.value = LoadingStateEnum.LOADING
        getConversationForMemberInteract(GetConversationForMemberInteract.Params(member)){
            it.either(fnR = fun(conversationList: List<ConversationEntity>) {
                if(!conversationList.isNullOrEmpty()) {
                    conversations.value = conversationList
                    state.value = LoadingStateEnum.DATA_FOUND
                } else {
                    state.value = LoadingStateEnum.NO_DATA_FOUND
                }
            }, fnL = fun(failure: Failure){
                when(failure) {
                    is GetConversationForMemberInteract.NoConversationFoundFailure -> state.postValue(LoadingStateEnum.NO_DATA_FOUND)
                    else ->  state.postValue(LoadingStateEnum.ERROR)
                }
            })
        }
    }

    /**
     * Delete Conversation By Id
     */
    fun delete(id: String) {
        deleteConversationInteract(DeleteConversationInteract.Params(id)){
            it.either(fnR = fun(_: String){
                result.value = OperationResultEnum.CONVERSATION_DELETED_SUCCESSFULLY
                if(conversations.value?.isEmpty() == true)
                    state.value = LoadingStateEnum.NO_DATA_FOUND
            }, fnL = fun(_: Failure){
                result.value = OperationResultEnum.DELETE_CONVERSATION_FAILED
            })
        }
    }

    /**
     * Delete All
     */
    fun deleteAll(memberId: String) {
        deleteAllConversationForMemberInteract(DeleteAllConversationForMemberInteract
                .Params(memberId)){
            it.either(fnR = fun(_: String){
                result.value = OperationResultEnum.ALL_CONVERSATION_DELETED_SUCCESSFULLY
                conversations.value = ArrayList()
                state.value = LoadingStateEnum.NO_DATA_FOUND
            }, fnL = fun(_: Failure){
                result.value = OperationResultEnum.DELETE_ALL_CONVERSATION_FAILED
            })
        }
    }

}