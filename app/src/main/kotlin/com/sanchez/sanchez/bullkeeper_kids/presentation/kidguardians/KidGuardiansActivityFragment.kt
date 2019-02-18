package com.sanchez.sanchez.bullkeeper_kids.presentation.kidguardians

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.KidGuardianComponent
import com.sanchez.sanchez.bullkeeper_kids.core.extension.invisible
import com.sanchez.sanchez.bullkeeper_kids.core.extension.visible
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.SupportRecyclerViewAdapter
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.decoration.ItemOffsetDecoration
import com.sanchez.sanchez.bullkeeper_kids.domain.models.KidGuardianEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.LoadingStateEnum
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_kid_guardian_list.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

/**
 * Kid Guardians Activity Fragment
 */
class KidGuardiansActivityFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener,
        SupportRecyclerViewAdapter.OnSupportRecyclerViewListener<KidGuardianEntity> {

    /**
     * Activity Handler
     */
    private lateinit var activityHandler: IKidGuardiansListHandler

    /**
     * Adapter
     */
    private lateinit var adapter: SupportRecyclerViewAdapter<KidGuardianEntity>

    /**
     * Context
     */
    @Inject
    internal lateinit var context: Context

    /**
     * View Model
     */
    @Inject
    internal lateinit var viewModel: KidGuardiansViewModel

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
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context !is IKidGuardiansListHandler)
            throw IllegalArgumentException("Context must implement Kid Guardians List Handler")

        activityHandler = context
    }

    /**
     * Layout Id
     */
    override fun layoutId(): Int = R.layout.fragment_kid_guardian_list

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInjector()

        stateObserverHandler()
        kidGuardianObserverHandler()
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
        adapter = KidGuardiansAdapter(currentActivity, ArrayList(), picasso)
        recyclerView.adapter = adapter
        adapter.setOnSupportRecyclerViewListener(this)

        noResultsFound.setOnClickListener {
            viewModel.load()
        }

        errorOcurred.setOnClickListener {
            viewModel.load()
        }

        // Load Conversations
        viewModel.load()

    }

    /**
     * Initialize Injector
     */
    fun initializeInjector() {
        val kidGuardianComponent = KidGuardianComponent::class.java
                .cast((activity as HasComponent<*>)
                        .component)
        kidGuardianComponent?.inject(this)
    }

    /**
     * On Item Click
     */
    override fun onItemClick(item: KidGuardianEntity) {
        activityHandler.onKidGuardianSelected(item)
    }

    /**
     * On Refresh
     */
    override fun onRefresh() = viewModel.load()

    /**
     * State Observer Handler
     */
    private fun stateObserverHandler(){

        val stateObserver = Observer<LoadingStateEnum> { loadingState ->
            when(loadingState) {
                // Loading
                LoadingStateEnum.LOADING -> {
                    loadingView.visible()
                    errorOcurred.invisible()
                    noResultsFound.invisible()
                    swipeRefreshLayout.invisible()
                }
                // No Data Found
                LoadingStateEnum.NO_DATA_FOUND -> {
                    loadingView.invisible()
                    errorOcurred.invisible()
                    noResultsFound.visible()
                    swipeRefreshLayout.invisible()
                }
                // Data Found
                LoadingStateEnum.DATA_FOUND -> {
                    loadingView.invisible()
                    errorOcurred.invisible()
                    noResultsFound.invisible()
                    swipeRefreshLayout.visible()
                    swipeRefreshLayout.isRefreshing = false
                }
                // Error
                LoadingStateEnum.ERROR -> {
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
     * Kid Guardian Observer Handler
     */
    private fun kidGuardianObserverHandler(){
        // Create the observer which updates the UI.
        val kidGuardianListObservable = Observer<List<KidGuardianEntity>> { kidGuardianList ->
            adapter.setData(kidGuardianList as MutableList<KidGuardianEntity>)
            adapter.notifyDataSetChanged()

        }
        viewModel.kidGuardians.observe(this, kidGuardianListObservable)
    }
}
