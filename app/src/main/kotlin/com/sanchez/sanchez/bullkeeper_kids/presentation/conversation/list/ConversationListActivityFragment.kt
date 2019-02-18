package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.list

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ConversationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.extension.invisible
import com.sanchez.sanchez.bullkeeper_kids.core.extension.visible
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.SupportItemTouchHelper
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.SupportRecyclerViewAdapter
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.decoration.ItemOffsetDecoration
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.ConfirmationDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.models.ConversationEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.LoadingStateEnum
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_conversation_list.*
import timber.log.Timber
import java.lang.IllegalArgumentException
import javax.inject.Inject

/**
 * Conversation List Activity Fragment
 */
class ConversationListActivityFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener,
        SupportItemTouchHelper.ItemTouchHelperListener, SupportRecyclerViewAdapter
        .OnSupportRecyclerViewListener<ConversationEntity> {

    /**
     * Activity Handler
     */
    private lateinit var activityHandler: IConversationListHandler

    /**
     * Adapter
     */
    private lateinit var adapter: SupportRecyclerViewAdapter<ConversationEntity>

    /**
     * Context
     */
    @Inject
    internal lateinit var context: Context

    /**
     * View Model
     */
    @Inject
    internal lateinit var viewModel: ConversationListViewModel

    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * Activity
     */
    @Inject
    internal lateinit var currentActivity: Activity

    /**
     * Picasso
     */
    @Inject
    internal lateinit var picasso: Picasso

    /**
     * Kid Identity
     */
    private val kid: String by lazy {
        preferenceRepository.getPrefKidIdentity()
    }

    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context !is IConversationListHandler)
            throw IllegalArgumentException("Context must implement IConversation Message List Handler")

        activityHandler = context
    }

    /**
     * Layout Id
     */
    override fun layoutId(): Int = R.layout.fragment_conversation_list

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInjector()

        stateObserverHandler()
        operationResultObserverHandler()
        conversationObserverHandler()
        // Config Swipe Refresh Layout
        swipeRefreshLayout.setColorSchemeResources(R.color.commonWhite)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.primaryDarkColor)
        swipeRefreshLayout.setOnRefreshListener(this)

        // Config Recycler View
        recyclerView.layoutManager = LinearLayoutManager(context)

        val itemOffsetDecoration = ItemOffsetDecoration(context, R.dimen.item_offset)
        recyclerView.addItemDecoration(itemOffsetDecoration)
        // Set Animator
        recyclerView.itemAnimator = DefaultItemAnimator()
        val selfUserId = preferenceRepository.getPrefKidIdentity()
        adapter = ConversationAdapter(currentActivity, ArrayList(), picasso, selfUserId)
        recyclerView.adapter = adapter
        adapter.setOnSupportRecyclerViewListener(this)

        addConversation.setOnClickListener {
            activityHandler.showKidGuardian()
        }

        deleteAllConversations.setOnClickListener {

            activityHandler.showConfirmationDialog(R.string.delete_all_conversations_confirm, object : ConfirmationDialogFragment.ConfirmationDialogListener {

                override fun onAccepted(dialog: DialogFragment) {
                    val memberOne = preferenceRepository.getPrefKidIdentity()
                    viewModel.deleteAll(memberOne)
                }

                override fun onRejected(dialog: DialogFragment) {}

            })

        }

        noResultsFound.setOnClickListener {
            viewModel.load(kid)
        }

        errorOcurred.setOnClickListener {
            viewModel.load(kid)
        }

        // adding item touch helper
        val itemTouchHelperCallback = SupportItemTouchHelper<ConversationAdapter.ConversationViewHolder>(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        // Load Conversations
        viewModel.load(kid)

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
     * On Swiped
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is ConversationAdapter.ConversationViewHolder) {

            val deletedIndex = viewHolder.adapterPosition
            val conversationEntity =
                    adapter.getItemByAdapterPosition(deletedIndex)

            // Delete item from adapter
            adapter.removeItem(deletedIndex)

            val snackBar = Snackbar.make(view!!,
                    getString(R.string.conversation_was_deleted), Snackbar.LENGTH_LONG)

            // Undo Action
            snackBar.setAction(getString(R.string.snackbar_undo_action)) {
                adapter.restoreItem(conversationEntity, deletedIndex)
            }

            // Add Callback
            snackBar.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if(event == DISMISS_EVENT_TIMEOUT) {
                        conversationEntity.identity
                                ?.let { viewModel.delete(it) }
                    }
                }
            })

            snackBar.show()
        }
    }

    /**
     * On Item Click
     */
    override fun onItemClick(item: ConversationEntity) {
        item.identity?.let { activityHandler.showConversationMessageList(it) }
    }

    /**
     * On Refresh
     */
    override fun onRefresh() = viewModel.load(kid)

    /**
     * State Observer Handler
     */
    private fun stateObserverHandler(){

        val stateObserver = Observer<LoadingStateEnum> { loadingState ->
            when(loadingState) {
                // Loading
                LoadingStateEnum.LOADING -> {
                    deleteAllConversations.invisible()
                    loadingView.visible()
                    errorOcurred.invisible()
                    noResultsFound.invisible()
                    swipeRefreshLayout.invisible()
                }
                // No Data Found
                LoadingStateEnum.NO_DATA_FOUND -> {
                    deleteAllConversations.invisible()
                    loadingView.invisible()
                    errorOcurred.invisible()
                    noResultsFound.visible()
                    swipeRefreshLayout.invisible()
                }
                // Data Found
                LoadingStateEnum.DATA_FOUND -> {
                    deleteAllConversations.visible()
                    loadingView.invisible()
                    errorOcurred.invisible()
                    noResultsFound.invisible()
                    swipeRefreshLayout.visible()
                    swipeRefreshLayout.isRefreshing = false
                }
                // Error
                LoadingStateEnum.ERROR -> {
                    deleteAllConversations.invisible()
                    loadingView.invisible()
                    errorOcurred.visible()
                    noResultsFound.invisible()
                    swipeRefreshLayout.invisible()
                }
            }
        }

        viewModel.state.observe(this, stateObserver)
    }


    /**
     * Operation Result Observer Handler
     */
    private fun operationResultObserverHandler(){

        val operationResultObserver = Observer<ConversationListViewModel.OperationResultEnum> { operationResult ->
            when(operationResult) {

                // Delete All Conversation Failed
                ConversationListViewModel.OperationResultEnum.DELETE_CONVERSATION_FAILED -> {

                }

                // Conversation Deleted Successfully
                ConversationListViewModel.OperationResultEnum.CONVERSATION_DELETED_SUCCESSFULLY -> {

                }

                else -> { }
            }
        }

        viewModel.result.observe(this, operationResultObserver)
    }

    /**
     * Conversation Observer Handler
     */
    private fun conversationObserverHandler(){
        // Create the observer which updates the UI.
        val conversationListObserver = Observer<List<ConversationEntity>> { conversationList ->
            adapter.setData(conversationList as MutableList<ConversationEntity>)
            adapter.notifyDataSetChanged()

        }
        viewModel.conversations.observe(this, conversationListObserver)
    }
}
