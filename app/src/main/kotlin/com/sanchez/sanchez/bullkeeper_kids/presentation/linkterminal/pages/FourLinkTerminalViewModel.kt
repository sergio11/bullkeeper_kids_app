package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls.SaveTerminalHistoryCallsInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.SynchronizeInstalledPackagesInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.sms.SaveSmsInTheTerminalInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import javax.inject.Inject

/**
 * Four View Model
 */
class FourLinkTerminalViewModel
    @Inject constructor(private val context: Application,
                        private val preferenceRepository: IPreferenceRepository,
                        private val synchronizeInstalledPackagesInteract: SynchronizeInstalledPackagesInteract,
                        private val saveSmsInTheTerminalInteract: SaveSmsInTheTerminalInteract,
                        private val saveTerminalHistoryCallsInteract: SaveTerminalHistoryCallsInteract)
    : BaseViewModel()  {


    // Total Apps Sync
    var totalAppsSync: MutableLiveData<Int> = MutableLiveData()

    // Error Sync Apps
    var errorSyncApps: MutableLiveData<Failure> = MutableLiveData()

    // Total Sms Sync
    var totalSmsSync: MutableLiveData<Int> = MutableLiveData()

    // Error Sync Sms
    var errorSyncSms: MutableLiveData<Failure> = MutableLiveData()

    // Total Calls Sync
    var totalCallsSync: MutableLiveData<Int> = MutableLiveData()

    // Error Sync Calls
    var errorSyncCalls: MutableLiveData<Failure> = MutableLiveData()


    /**
     * Sync Apps
     */
    fun syncApps(){
        // Sync Installed Packages
        synchronizeInstalledPackagesInteract(UseCase.None()) {
            it.either(::onApplicationsSynchronizationFailed, ::onFinishedApplicationsSynchronization)
        }
    }

    /**
     * On Applications Synchronization Failed
     */
    private fun onApplicationsSynchronizationFailed(failure: Failure) {
        Preconditions.checkNotNull(failure, "Failure can not be null")
        errorSyncApps.value = failure
    }

    /**
     * On Finished Application Synchronization
     */
    private fun onFinishedApplicationsSynchronization(totalAppsSync: Int) {
        this.totalAppsSync.value = totalAppsSync
    }

    /**
     * Sync Sms
     */
    fun syncSms(){
        // Sync SMS
        saveSmsInTheTerminalInteract(UseCase.None()) {
            it.either(::onSmsSynchronizationFailed, ::onFinishedSmsSynchronization)
        }
    }


    /**
     * On SMS Synchronization Failed
     */
    private fun onSmsSynchronizationFailed(failure: Failure) {
        Preconditions.checkNotNull(failure, "Failure can not be null")
        errorSyncSms.value = failure
    }

    /**
     * On Finished SMS synchronization
     */
    private fun onFinishedSmsSynchronization(totalSmsSync: Int) {
        this.totalSmsSync.value = totalSmsSync
    }

    /**
     * Sync Calls
     */
    fun syncCalls(){
        // Sync History Calls
        saveTerminalHistoryCallsInteract(UseCase.None()) {
            it.either(::onHistoryCallSynchronizationFailed, ::onFinishedCallsSynchronization)
        }
    }


    /**
     * On Calls Synchronization Failed
     */
    private fun onHistoryCallSynchronizationFailed(failure: Failure) {
        Preconditions.checkNotNull(failure, "Failure can not be null")
        errorSyncCalls.value = failure
    }

    /**
     * On Finished Calls synchronization
     */
    private fun onFinishedCallsSynchronization(totalCallsSync: Int) {
        this.totalCallsSync.value = totalCallsSync
    }

}