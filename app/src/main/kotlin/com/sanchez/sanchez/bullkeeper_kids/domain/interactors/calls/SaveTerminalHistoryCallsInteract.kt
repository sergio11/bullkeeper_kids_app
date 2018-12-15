package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls

import android.annotation.SuppressLint
import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ICallsService
import retrofit2.Retrofit
import javax.inject.Inject
import android.provider.CallLog
import com.sanchez.sanchez.bullkeeper_kids.core.extension.batch
import com.sanchez.sanchez.bullkeeper_kids.data.entity.CallDetailEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveCallDetailDTO
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.CallDetailRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import timber.log.Timber
import java.util.*


/**
 * Save Terminal History Calls Interact
 */
class SaveTerminalHistoryCallsInteract
    @Inject constructor(
            private val context: Context,
            private val callService: ICallsService,
            private val callDetailsRepositoryImpl: CallDetailRepositoryImpl,
            private val preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit): UseCase<Int, UseCase.None>(retrofit) {

    companion object {
        val BATCH_SIZE = 5
    }

    /**
     * Get Call Details
     */
    @SuppressLint("MissingPermission")
    private fun getCallDetails(): List<CallDetailEntity> {
        val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC")


        val id = cursor.getColumnIndex(CallLog.Calls._ID)
        val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
        val type = cursor.getColumnIndex(CallLog.Calls.TYPE)
        val date = cursor.getColumnIndex(CallLog.Calls.DATE)
        val duration = cursor.getColumnIndex(CallLog.Calls.DURATION)


        var historyCalls = arrayListOf<CallDetailEntity>()
        while (cursor.moveToNext()) {
            val callDetail = CallDetailEntity()
            callDetail.id = cursor.getString(id)
            callDetail.phoneNumber = cursor.getString(number)
            val callType = cursor.getString(type)
            val callDate = cursor.getString(date)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = java.lang.Long.valueOf(callDate)
            callDetail.callDayTime = calendar.time
            callDetail.callDuration = cursor.getString(duration)
            var dir: String? = null
            val dircode = Integer.parseInt(callType)
            when (dircode) {
                CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
            }
            callDetail.callType = dir
            historyCalls.add(callDetail)
        }
        cursor.close()
        return historyCalls
    }

    /**
     * On Execute
     */
    override suspend fun onExecuted(params: None): Int {

        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()
        // Get Call Details
        val callDetails = getCallDetails()

        var totalCallsSync = 0

        if(callDetails.isNotEmpty()) {

            // Save Call Details
            callDetailsRepositoryImpl.save(callDetails)
            Timber.d("Total calls saved -> %d", callDetails.size)

            val callDetailSyncList = arrayListOf<CallDetailEntity>()

            callDetails.asSequence().batch(BATCH_SIZE).forEach { group ->

                val response = callService
                        .saveHistoryCallsInTheTerminal(kid, terminal, group.map { SaveCallDetailDTO(it.phoneNumber,
                                it.callDayTime, it.callDuration, it.callType, it.id) })
                        .await()

                response.httpStatus?.let {

                    if(it == "OK") {

                        response.data?.forEach {callDetailDTO ->
                            group.map {
                                if(it.id == callDetailDTO.localId) {
                                    it.serverId = callDetailDTO.identity
                                    it.sync = 1
                                }
                            }
                        }
                        // Save Sync Calls
                        callDetailsRepositoryImpl.save(group)
                        // Add To List
                        callDetailSyncList.addAll(group)
                    } else {
                        Timber.d("No Success Sync SMS")
                    }

                }
            }

            totalCallsSync = callDetailSyncList.size

        }

        return totalCallsSync
    }
}