package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.LinkDeviceTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportPageFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.SupportRecyclerViewAdapter
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.decoration.ItemOffsetDecoration
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.children.GetSelfChildrenInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SonEntity
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.adapter.ChildrenAdapter
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.ILinkDeviceTutorialHandler
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.second_link_terminal_page_fragment_layout.*
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

/**
 * Second Link Terminal Page Fragment
 */
class SecondLinkTerminalPageFragment: SupportPageFragment<LinkDeviceTutorialComponent>(), SwipeRefreshLayout.OnRefreshListener, SupportRecyclerViewAdapter.OnSupportRecyclerViewListener<SonEntity> {

    /**
     * Dependencies
     */

    // Picasso
    @Inject lateinit var picasso: Picasso

    // Second Link Terminal View Model
    @Inject lateinit var secondLinkTerminalViewModel: SecondLinkTerminalViewModel

    // Activity
    @Inject lateinit var currentActivity: Activity

    /**
     * Link Device Tutorial Handler
     */
    lateinit var linkDeviceTutorialHandler: ILinkDeviceTutorialHandler

    /**
     * Children Adapter
     */
    lateinit var childrenAdapter: ChildrenAdapter

    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int = R.layout.second_link_terminal_page_fragment_layout


    /**
     * Load Data
     */
    private fun loadData() {
        showLoadingState()
        // Load Children List
        secondLinkTerminalViewModel.loadChildrenList()
    }

    /**
     * Initialize Injector
     */
    override fun initializeInjector(): LinkDeviceTutorialComponent {
        val linkDeviceTutorialComponent =
                LinkDeviceTutorialComponent::class.java
                .cast((activity as HasComponent<LinkDeviceTutorialComponent>)
                        .component)
        linkDeviceTutorialComponent.inject(this)
        return linkDeviceTutorialComponent
    }

    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        /**
         * Check Context
         */
        if(context !is ILinkDeviceTutorialHandler)
            throw IllegalStateException("The context does not implement the handler ILinkDeviceTutorialHandler")

        linkDeviceTutorialHandler = context

    }

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Config Swipe Refresh Layout
        swipeRefreshLayout.setColorSchemeResources(R.color.commonWhite)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.primaryDarkColor)
        swipeRefreshLayout.setOnRefreshListener(this)

        // Config Recycler View
        recyclerView.layoutManager = LinearLayoutManager(context)

        val itemOffsetDecoration = ItemOffsetDecoration(context!!, R.dimen.item_offset)
        recyclerView.addItemDecoration(itemOffsetDecoration)
        // Set Animator
        recyclerView.itemAnimator = DefaultItemAnimator()
        childrenAdapter = ChildrenAdapter(currentActivity, ArrayList(), picasso)
        recyclerView.adapter = childrenAdapter
        childrenAdapter.setOnSupportRecyclerViewListener(this)

        // Create the observer which updates the UI.
        val childrenListObserver = Observer<List<SonEntity>> { childrenList ->
            swipeRefreshLayout.isRefreshing = false
            childrenAdapter.setData(childrenList as MutableList<SonEntity>)
            childrenAdapter.notifyDataSetChanged()
            showResultsState()
        }

        // Create the observer which updates the UI.
        val childrenLoadedFailureObserver = Observer<Failure> { failure ->
            when(failure) {
                is GetSelfChildrenInteract.NoChildrenFoundFailure -> showNoResultsState()
                else ->  showErrorState()
            }
        }

        secondLinkTerminalViewModel.childrenList.observe(this, childrenListObserver)
        secondLinkTerminalViewModel.loadChildrenFailure.observe(this, childrenLoadedFailureObserver)


        loadingError.setOnClickListener { loadData() }
        noResultsFound.setOnClickListener { loadData() }

        loadData()

    }


    /**
     * Show No Results State
     */
    private fun showNoResultsState() {
        Timber.d("VIEWS: Show No Results State")
        loadingView.visibility = View.GONE
        swipeRefreshLayout.visibility = View.GONE
        loadingError.visibility = View.GONE
        noResultsFound.visibility = View.VISIBLE
    }

    /**
     * Show Scan Devices State
     */
    private fun showLoadingState() {
        Timber.d("VIEWS: Show Loading State")
        noResultsFound.visibility = View.GONE
        swipeRefreshLayout.visibility = View.GONE
        loadingError.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
    }

    /**
     * Show Results State
     */
    private fun showResultsState() {
        Timber.d("VIEWS: Show Results State")
        noResultsFound.visibility = View.GONE
        loadingView.visibility = View.GONE
        loadingError.visibility = View.GONE
        swipeRefreshLayout.visibility = View.VISIBLE
    }

    /**
     * Show Scan Error State
     */
    private fun showErrorState() {
        Timber.d("VIEWS: Show Error State")
        noResultsFound.visibility = View.GONE
        loadingView.visibility = View.GONE
        swipeRefreshLayout.visibility = View.GONE
        loadingError.visibility = View.VISIBLE
    }


    /**
     * When Phase Is Hidden
     */
    override fun whenPhaseIsHidden(pagePosition: Int, currentPosition: Int) {
        Timber.d("Phase Is Hidden")

        if(currentPosition > pagePosition && linkDeviceTutorialHandler.getCurrentSonEntity() == null)
            linkDeviceTutorialHandler.showNoticeDialog(R.string.select_child_before_continuing, object : NoticeDialogFragment.NoticeDialogListener {
                override fun onAccepted(dialog: DialogFragment) {
                    linkDeviceTutorialHandler.requestFocus()
                }
            })


    }

    /**
     * When Phase Is Showed
     */
    override fun whenPhaseIsShowed() {
        Timber.d("Phase Is Showed")
    }


    /**
     * Get Transform Items
     */
    override fun getTransformItems(): Array<TransformItem> {
        return arrayOf(
                TransformItem.create(R.id.titleText, Direction.LEFT_TO_RIGHT, 0.2f),
                TransformItem.create(R.id.contentText, Direction.RIGHT_TO_LEFT, 0.7f),
                TransformItem.create(R.id.childrenContainer, Direction.LEFT_TO_RIGHT, 0.7f)
        )
    }

    /**
     * On Refresh
     */
    override fun onRefresh() = loadData()

    /**
     * On Item Click
     */
    override fun onItemClick(item: SonEntity) {
        Preconditions.checkNotNull(item, "Item can not be null")
        linkDeviceTutorialHandler.setCurrentSonEntity(item)
        linkDeviceTutorialHandler.releaseFocus()
    }

}