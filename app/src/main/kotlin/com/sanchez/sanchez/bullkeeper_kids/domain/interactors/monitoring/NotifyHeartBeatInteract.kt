package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.monitoring

import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.NotifyTerminalHeartBeatDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IHeartBeatService
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Notify Heart Beat Interact
 */
class NotifyHeartBeatInteract
    @Inject constructor(retrofit: Retrofit,
                        private val context: Context,
                        private val preferenceRepository: IPreferenceRepository,
                        private val heartBeatService: IHeartBeatService)
        : UseCase<String, UseCase.None>(retrofit) {

    /**
     * Get Screen Status
     */
    private fun getScreenStatus(): String {
        val dm = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        return when(dm.getDisplay(Display.DEFAULT_DISPLAY).state) {
            0 -> "STATE_UNKNOWN"
            1 -> "STATE_OFF"
            2 -> "STATE_ON"
            3 -> "STATE_DOZE"
            4 -> "STATE_DOZE_SUSPEND"
            6 -> "STATE_ON_SUSPEND"
            else -> "STATE_UNKNOWN"
        }
    }


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None): String {

        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()
        val screenStatus = getScreenStatus()

        val response = heartBeatService.notifyTerminalHeartbeat(kid, terminal ,
                NotifyTerminalHeartBeatDTO(
                        kid = kid,
                        screenStatus = screenStatus,
                        terminal = terminal,
                        accessFineLocationEnabled = preferenceRepository.isAccessFineLocationEnabled(),
                        readContactsEnabled = preferenceRepository.isReadContactsEnabled(),
                        readCallLogEnabled = preferenceRepository.isReadCallLogEnabled(),
                        writeExternalStorageEnabled = preferenceRepository.isWriteExternalStorageEnabled(),
                        usageStatsAllowed = preferenceRepository.isUsageStatsAllowed(),
                        adminAccessEnabled = preferenceRepository.isAdminAccessEnabled(),
                        batteryLevel = preferenceRepository.getBatteryLevel(),
                        isBatteryCharging = preferenceRepository.isBatteryChargingEnabled(),
                        highAccuraccyLocation = preferenceRepository.isHighAccuraccyLocationEnabled())).await()

        return response.data!!
    }


}