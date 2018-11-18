package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.fernandocejas.arrow.checks.Preconditions
import com.jaredrummler.android.device.DeviceName
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.SaveTerminalInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.TerminalEntity
import javax.inject.Inject

/**
 * Third View Model
 */
class ThirdLinkTerminalViewModel
    @Inject constructor(private val context: Application,
                        private val saveTerminalInteract: SaveTerminalInteract) : BaseViewModel()  {


    /**
     * Live Data
     */

    // Device Info
    var deviceInfo: MutableLiveData<DeviceName.DeviceInfo> = MutableLiveData()

    // terminal saved
    var terminalSaved: MutableLiveData<TerminalEntity> = MutableLiveData()

    // Error Save Terminal
    var errorSaveTerminal: MutableLiveData<Failure> = MutableLiveData()


    /**
     * Methods
     */

    /**
     * Get Device Information
     */
    fun getDeviceInformation() = DeviceName.with(context).request { info, error ->
        deviceInfo.value = info
    }

    /**
     * Save Terminal
     */
    fun saveTerminal(sonId: String, appVersionName: String, appVersionCode: String,
                     manufacturer: String, marketName: String, codeName: String, name: String,
                     deviceName: String, model: String, osVersion: String, sdkVersion: String) {

        saveTerminalInteract(SaveTerminalInteract.Params(
                sonId = sonId,
                appVersionName = appVersionName,
                appVersionCode = appVersionCode,
                manufacturer = manufacturer,
                marketName = marketName,
                codeName = codeName,
                name = name,
                deviceName = deviceName,
                model = model,
                osVersion = osVersion,
                sdkVersion = sdkVersion
        )) {
            it.either(::onSaveTerminalFailed, ::onSaveTerminalSuccess)
        }
    }


    /**
     * Handlers
     */

    fun onSaveTerminalFailed(failure: Failure) {
        Preconditions.checkNotNull(failure, "Failure can not be null")
        errorSaveTerminal.value = failure
    }

    /**
     * On Save Terminal Success
     */
    fun onSaveTerminalSuccess(terminalEntity: TerminalEntity) {
        Preconditions.checkNotNull(terminalEntity, "Terminal can not be null")
        terminalSaved.value = terminalEntity
    }

}