package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.SynchronizeInstalledPackagesInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import javax.inject.Inject

/**
 * Four View Model
 */
class FourLinkTerminalViewModel
    @Inject constructor(private val context: Application,
                        private val preferenceRepository: IPreferenceRepository,
                        private val synchronizeInstalledPackagesInteract: SynchronizeInstalledPackagesInteract)
    : BaseViewModel()  {


    // Total Apps Sync
    var totalAppsSync: MutableLiveData<Int> = MutableLiveData()

    // Error Sync Apps
    var errorSyncApps: MutableLiveData<Failure> = MutableLiveData()


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
     * On Terminal Detail Failed
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

}