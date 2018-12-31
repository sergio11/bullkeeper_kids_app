package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.provider.Settings
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.GetTerminalDetailInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.TerminalEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * Link Device Tutorial View Model
 */
class FirstLinkTerminalViewModel
    @Inject constructor(
            private val context: Application,
            private val getTerminalDetailInteract: GetTerminalDetailInteract,
            private val preferenceRepository: IPreferenceRepository)
    : BaseViewModel()  {

    /**
     * Terminal Succes
     */
    var terminalSuccess: MutableLiveData<TerminalEntity> = MutableLiveData()

    /**
     * Terminal Failure
     */
    var terminalFailure: MutableLiveData<Failure> = MutableLiveData()


    /**
     * Check Terminal Status
     */
    fun checkTerminalStatus() {

        if(preferenceRepository.getPrefKidIdentity()
                != IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE) {
            val kid = preferenceRepository.getPrefKidIdentity()
            val deviceId = Settings.Secure.getString(context.contentResolver,
                    Settings.Secure.ANDROID_ID)
            // Get Terminal Detail
            getTerminalDetailInteract(GetTerminalDetailInteract.Params(kid, deviceId)) {
                it.either(::onTerminalDetailFailed, ::onTerminalDetailSuccess)
            }
        } else {
            Timber.d("No Children Linked Failure")
            terminalFailure.value = NoChildrenLinkedFailure()
        }

    }

    /**
     * On Terminal Detail Failed
     */
    private fun onTerminalDetailFailed(failure: Failure) {
        Preconditions.checkNotNull(failure, "Failure can not be null")
        Timber.d("Terminal Detail Failure")
        terminalFailure.value = failure
    }

    /**
     * On Terminal Detail Success
     */
    private fun onTerminalDetailSuccess(terminalEntity: TerminalEntity) {
        Preconditions.checkNotNull(terminalEntity, "Terminal Detail can not be null")

        Timber.d("Terminal Detail Success")
        terminalEntity.identity?.let{
            preferenceRepository.setPrefTerminalIdentity(it)
        }

        terminalEntity.deviceId?.let{
            preferenceRepository.setPrefDeviceId(it)
        }

        terminalEntity.kidId?.let{
            preferenceRepository.setPrefKidIdentity(it)
        }

        preferenceRepository.setCameraEnabled(terminalEntity.lockCameraEnabled)
        preferenceRepository.setLockScreenEnabled(terminalEntity.lockScreenEnabled)
        preferenceRepository.setBedTimeEnabled(terminalEntity.bedTimeEnabled)

        terminalSuccess.value = terminalEntity
    }

    class NoChildrenLinkedFailure: Failure.FeatureFailure()

}