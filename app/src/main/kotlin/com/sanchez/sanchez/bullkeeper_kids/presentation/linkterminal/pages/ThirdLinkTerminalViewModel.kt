package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.provider.Settings
import com.fernandocejas.arrow.checks.Preconditions
import com.jaredrummler.android.device.DeviceName
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.GetTerminalDetailInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.SaveTerminalInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.TerminalEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import javax.inject.Inject

/**
 * Third View Model
 */
class ThirdLinkTerminalViewModel
    @Inject constructor(private val context: Application,
                        private val saveTerminalInteract: SaveTerminalInteract,
                        private val getTerminalDetailInteract: GetTerminalDetailInteract,
                        private val preferenceRepository: IPreferenceRepository) : BaseViewModel()  {

    /**
     * Live Data
     */

    // Device Info
    var deviceInfo: MutableLiveData<DeviceName.DeviceInfo> = MutableLiveData()

    // terminal saved
    var terminalSaved: MutableLiveData<TerminalEntity> = MutableLiveData()

    // Error Save Terminal
    var errorSaveTerminal: MutableLiveData<Failure> = MutableLiveData()

    // No Terminal Linked
    var noTerminalLinked: MutableLiveData<Failure> = MutableLiveData()


    /**
     * Methods
     */

    override fun init() {}

    /**
     * Get Device Information
     */
    fun getDeviceInformation() = DeviceName.with(context).request { info, error ->
        deviceInfo.value = info
    }

    /**
     * Check Terminal Status
     */
    fun checkTerminalStatus(kidId: String) {
        Preconditions.checkNotNull(kidId, "Kid id can not be null")
        Preconditions.checkState(!kidId.isNullOrEmpty(), "Kid id can not be null or empty")
        val deviceId = Settings.Secure.getString(context.contentResolver,
                Settings.Secure.ANDROID_ID)
        // Get Terminal Detail
        getTerminalDetailInteract(GetTerminalDetailInteract.Params(kidId, deviceId)) {
            it.either(::onTerminalDetailFailed, ::onTerminalDetailSuccess)
        }

    }

    /**
     * Save Terminal
     */
    fun saveTerminal(kidId: String, appVersionName: String, appVersionCode: String,
                     manufacturer: String, marketName: String, codeName: String, name: String,
                     deviceName: String, deviceId: String, model: String, osVersion: String, sdkVersion: String) {

        saveTerminalInteract(SaveTerminalInteract.Params(
                kidId = kidId,
                appVersionName = appVersionName,
                appVersionCode = appVersionCode,
                manufacturer = manufacturer,
                marketName = marketName,
                codeName = codeName,
                name = name,
                deviceName = deviceName,
                deviceId = deviceId,
                model = model,
                osVersion = osVersion,
                sdkVersion = sdkVersion
        )) {
            it.either(::onSaveTerminalFailed, ::onTerminalDetailSuccess)
        }
    }


    /**
     * Handlers
     */

    private fun onSaveTerminalFailed(failure: Failure) {
        Preconditions.checkNotNull(failure, "Failure can not be null")
        errorSaveTerminal.value = failure
    }

    /**
     * On Terminal Detail Failed
     */
    private fun onTerminalDetailFailed(failure: Failure) {
        Preconditions.checkNotNull(failure, "Failure can not be null")
        noTerminalLinked.value = failure
    }

    /**
     * On Terminal Detail Success
     */
    private fun onTerminalDetailSuccess(terminalEntity: TerminalEntity) {
        Preconditions.checkNotNull(terminalEntity, "Terminal Detail can not be null")

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
        preferenceRepository.setScreenEnabled(terminalEntity.lockScreenEnabled)
        preferenceRepository.setBedTimeEnabled(terminalEntity.bedTimeEnabled)
        preferenceRepository.setSettingsEnabled(terminalEntity.settingsEnabled)

        terminalSaved.value = terminalEntity
    }

}