package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.LinkDeviceTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportPageFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.ILinkDeviceTutorialHandler
import kotlinx.android.synthetic.main.four_link_terminal_page_fragment_layout.*
import timber.log.Timber
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject


/**
 * Four Link Terminal Page Fragment
 */
class FourLinkTerminalPageFragment: SupportPageFragment<LinkDeviceTutorialComponent>() {

    /**
     * Link Device Tutorial Handler
     */
    lateinit var linkDeviceTutorialHandler: ILinkDeviceTutorialHandler

    /**
     * Dependencies
     * ==================
     */

    // Four Link Terminal View Model
    @Inject
    lateinit var fourLinkTerminalViewModel: FourLinkTerminalViewModel

    /**
     * Preference Repository
     */
    @Inject
    lateinit var preferenceRepository: IPreferenceRepository

    /**
     * App Context
     */
    @Inject
    lateinit var appContext: Context

    private val IS_SYNC = "IS_SYNC"
    private val FINISHED = "FINISHED"
    private val SYNC_APPS_POS = 0
    private val SYNC_SMS_POS = 1
    private val SYNC_HISTORY_CALL_POS = 2
    private val SYNC_CONTACTS_POS = 3

    // sync results
    private var syncResults = hashMapOf(
            SYNC_APPS_POS to hashMapOf(
                    IS_SYNC to false,
                    FINISHED to false
            ),
            SYNC_SMS_POS to hashMapOf(
                    IS_SYNC to false,
                    FINISHED to false
            ),
            SYNC_HISTORY_CALL_POS to hashMapOf(
                    IS_SYNC to false,
                    FINISHED to false
            ),
            SYNC_CONTACTS_POS to hashMapOf(
                    IS_SYNC to false,
                    FINISHED to false
            ))


    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int
            = R.layout.four_link_terminal_page_fragment_layout

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
     * Start Sync
     */
    private fun startSync(){
        linkDeviceTutorialHandler.showProgressDialog(R.string.generic_loading_text)
        retrySync.visibility = INVISIBLE
        retrySync.isEnabled = false
        syncResults.entries.filter { !it.value[IS_SYNC]!! }.forEach {
            when(it.key) {
                SYNC_APPS_POS -> startSyncApps()
                SYNC_SMS_POS -> startSyncSms()
                SYNC_HISTORY_CALL_POS -> startSyncHistoryCalls()
                SYNC_CONTACTS_POS -> startSyncContacts()
            }
        }

    }

    /**
     * Check Sync Finished
     */
    private fun checkSyncFinished(){
        if(!syncResults.entries.any { !it.value[FINISHED]!! }) {
            linkDeviceTutorialHandler.hideProgressDialog()
            if(syncResults.entries.any { !it.value[IS_SYNC]!! }) {
                retrySync.visibility = VISIBLE
                retrySync.isEnabled = true
            } else {
                linkDeviceTutorialHandler.showNoticeDialog(R.string.synchronization_completed, object : NoticeDialogFragment.NoticeDialogListener {
                    override fun onAccepted(dialog: DialogFragment) {
                        linkDeviceTutorialHandler.releaseFocus()
                    }
                })
            }
        }
    }



    /**
     * Start Sync Apps
     */
    private fun startSyncApps(){
        syncResults[SYNC_APPS_POS]?.set(FINISHED, false)
        context?.let { it ->
            totalAppsSyncTextView.text = it.getString(R.string.synchronizing_applications_installed)
        }
        syncAppsInstalled.visibility = VISIBLE
        fourLinkTerminalViewModel.syncApps()
    }

    /**
     * Start Sync History Calls
     */
    private fun startSyncHistoryCalls(){
        syncResults[SYNC_HISTORY_CALL_POS]?.set(FINISHED, false)
        context?.let { it ->
            totalCallsSyncTextView.text = it.getString(R.string.synchronizing_call_history)
        }
        syncHistoryCalls.visibility = VISIBLE
        fourLinkTerminalViewModel.syncCalls()
    }

    /**
     * Start Sync Sms
     */
    private fun startSyncSms(){
        syncResults[SYNC_SMS_POS]?.set(FINISHED, false)
        context?.let { it ->
            totalSmsSyncTextView.text = it.getString(R.string.synchronizing_sms)
        }
        syncSms.visibility = VISIBLE
        fourLinkTerminalViewModel.syncSms()
    }

    /**
     * Start Sync Contacts
     */
    private fun startSyncContacts(){
        syncResults[SYNC_CONTACTS_POS]?.set(FINISHED, false)
        context?.let { it ->
            totalContactsSyncTextView.text = it.getString(R.string.synchronizing_contacts)
        }
        syncContacts.visibility = VISIBLE
        fourLinkTerminalViewModel.syncContacts()
    }

    /**
     * On View Create
     */
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        retrySync.setOnClickListener {
            startSync()
        }

        // Observe Apps
        val totalAppsSyncObserver = Observer<Int> { totalApps ->
            context?.let {
                totalAppsSyncTextView.text = String.format(Locale.getDefault(),
                        it.getString(R.string.successfully_synchronized_applications), totalApps)
            }
            syncResults[SYNC_APPS_POS]?.set(IS_SYNC, true)
            syncResults[SYNC_APPS_POS]?.set(FINISHED, true)
            checkSyncFinished()
        }

        fourLinkTerminalViewModel.totalAppsSync.observe(this, totalAppsSyncObserver)

        val errorSyncAppsObserver = Observer<Failure> {
            context?.let { it ->
                totalAppsSyncTextView.text = it.getString(R.string.error_ocurred_during_synchronization)
            }
            syncResults[SYNC_APPS_POS]?.set(IS_SYNC, false)
            syncResults[SYNC_APPS_POS]?.set(FINISHED, true)
            checkSyncFinished()
        }

        fourLinkTerminalViewModel.errorSyncApps.observe(this, errorSyncAppsObserver)

        // Observe Sms
        val totalSmsSyncObserver = Observer<Int> {totalSms ->
            context?.let {
                totalSmsSyncTextView.text = String.format(Locale.getDefault(),
                        it.getString(R.string.sms_successfully_synchronized), totalSms)
            }
            syncResults[SYNC_SMS_POS]?.set(IS_SYNC, true)
            syncResults[SYNC_SMS_POS]?.set(FINISHED, true)
            checkSyncFinished()
        }

        fourLinkTerminalViewModel.totalSmsSync.observe(this, totalSmsSyncObserver)

        val errorSyncSmsObserver = Observer<Failure> {
            context?.let { it ->
                totalSmsSyncTextView.text = it.getString(R.string.error_ocurred_during_synchronization)
            }
            syncResults[SYNC_SMS_POS]?.set(IS_SYNC, false)
            syncResults[SYNC_SMS_POS]?.set(FINISHED, true)
            checkSyncFinished()
        }

        fourLinkTerminalViewModel.errorSyncSms.observe(this, errorSyncSmsObserver)

        // Total Calls Sync
        val totalCallsSyncObserver = Observer<Int> {totalCalls ->
            context?.let {
                totalCallsSyncTextView.text = String.format(Locale.getDefault(),
                        it.getString(R.string.successfully_synchronized_call_history), totalCalls)
            }

            syncResults[SYNC_HISTORY_CALL_POS]?.set(IS_SYNC, true)
            syncResults[SYNC_HISTORY_CALL_POS]?.set(FINISHED, true)
            checkSyncFinished()

        }

        fourLinkTerminalViewModel.totalCallsSync.observe(this, totalCallsSyncObserver)

        val errorSyncCallsObserver = Observer<Failure> {
            context?.let { it ->
                totalCallsSyncTextView.text = it.getString(R.string.error_ocurred_during_synchronization)
            }
            syncResults[SYNC_HISTORY_CALL_POS]?.set(IS_SYNC, false)
            syncResults[SYNC_HISTORY_CALL_POS]?.set(FINISHED, true)
            checkSyncFinished()
        }

        fourLinkTerminalViewModel.errorSyncCalls.observe(this, errorSyncCallsObserver)


        // Total Contacts Sync
        val totalContactsSyncObserver = Observer<Int> {totalContacts ->
            context?.let {
                totalContactsSyncTextView.text = String.format(Locale.getDefault(),
                        it.getString(R.string.successfully_synchronized_contacts), totalContacts)
            }

            syncResults[SYNC_CONTACTS_POS]?.set(IS_SYNC, true)
            syncResults[SYNC_CONTACTS_POS]?.set(FINISHED, true)
            checkSyncFinished()

        }

        fourLinkTerminalViewModel.totalContactsSync.observe(this, totalContactsSyncObserver)

        val errorSyncContactsObserver = Observer<Failure> {
            context?.let { it ->
                totalContactsSyncTextView.text = it.getString(R.string.error_ocurred_during_synchronization)
            }
            syncResults[SYNC_CONTACTS_POS]?.set(IS_SYNC, false)
            syncResults[SYNC_CONTACTS_POS]?.set(FINISHED, true)
            checkSyncFinished()
        }

        fourLinkTerminalViewModel.errorSyncContacts.observe(this, errorSyncContactsObserver)


    }

    /**
     * When Phase Is Hidden
     */
    override fun whenPhaseIsHidden(pagePosition: Int, currentPosition: Int) {
        Timber.d("Phase Is Hidden")

        if(currentPosition > pagePosition ) {
            if(syncResults.entries.any { !it.value[IS_SYNC]!! }) {
                linkDeviceTutorialHandler.showNoticeDialog(R.string.synchronized_later_time)
            }
        }
    }

    /**
     * When Phase Is Showed
     */
    @SuppressLint("SetTextI18n", "HardwareIds")
    override fun whenPhaseIsShowed() {
        Timber.d("Phase Is Showed")
        if(preferenceRepository.getPrefTerminalIdentity() !=
                IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE &&
                preferenceRepository.getPrefKidIdentity() !=
                IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE) {
                startSync()
        }
    }


    /**
     * Get Transform Items
     */
    override fun getTransformItems(): Array<TransformItem> {
        return arrayOf(
                TransformItem.create(R.id.titleText,
                        Direction.LEFT_TO_RIGHT, 0.2f),
                TransformItem.create(R.id.syncAppsInstalled,
                        Direction.RIGHT_TO_LEFT, 0.2f),
                TransformItem.create(R.id.syncSms,
                        Direction.LEFT_TO_RIGHT, 0.2f),
                TransformItem.create(R.id.syncHistoryCalls,
                        Direction.RIGHT_TO_LEFT, 0.2f),
                TransformItem.create(R.id.syncContacts,
                        Direction.LEFT_TO_RIGHT, 0.2f),
                TransformItem.create(R.id.retrySync,
                        Direction.RIGHT_TO_LEFT, 0.2f)
        )
    }

}