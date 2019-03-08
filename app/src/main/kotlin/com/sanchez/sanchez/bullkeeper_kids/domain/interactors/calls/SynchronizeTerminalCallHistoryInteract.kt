package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls

import android.annotation.SuppressLint
import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ICallsService
import retrofit2.Retrofit
import javax.inject.Inject
import android.provider.CallLog
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.extension.batch
import com.sanchez.sanchez.bullkeeper_kids.core.extension.toDateTimeString
import com.sanchez.sanchez.bullkeeper_kids.data.entity.CallDetailEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveCallDetailDTO
import com.sanchez.sanchez.bullkeeper_kids.data.repository.ICallDetailRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.CallDetailRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import timber.log.Timber


/**
 * Synchronize Terminal Call History Interact
 */
class SynchronizeTerminalCallHistoryInteract
    @Inject constructor(
            private val context: Context,
            private val callService: ICallsService,
            private val callDetailsRepositoryImpl: ICallDetailRepository,
            private val preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit): UseCase<Int, UseCase.None>(retrofit) {

    val TAG = "SYNC_TERMINAL_CALL_HISTORY"

    companion object {
        val BATCH_SIZE = 15
    }

    /**
     * callsRegisteredInTheTerminal
     */
    @SuppressLint("MissingPermission")
    private fun getCallsRegisteredInTheTerminal(): List<CallDetailEntity> {
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
            callDetail.callDayTime = callDate.toLong().toDateTimeString()
            callDetail.callDuration = cursor.getString(duration)
            val dircode = Integer.parseInt(callType)
            callDetail.callType = when (dircode) {
                CallLog.Calls.OUTGOING_TYPE -> "OUTGOING"
                CallLog.Calls.INCOMING_TYPE -> "INCOMING"
                CallLog.Calls.MISSED_TYPE -> "MISSED"
                else -> "UNKNOWN"

            }
            historyCalls.add(callDetail)
        }
        cursor.close()
        return historyCalls.filter { !it.phoneNumber.isNullOrEmpty() }
    }

    /**
     * Get Calls To Save
     */
    private fun getCallsToSave(callsRegistered: List<CallDetailEntity>,
                               callsSaved: List<CallDetailEntity>): List<CallDetailEntity> {

        val callsToSave = arrayListOf<CallDetailEntity>()

        for(callRegistered in callsRegistered) {

            var isFound = false
            for(callSaved in callsSaved) {
                if(callRegistered.id == callSaved.id) {
                    isFound = true
                    if(callSaved.sync == 0 || callSaved.remove == 1 )
                        callsToSave.add(callSaved)
                }
            }

            if(!isFound)
                callsToSave.add(callRegistered)
        }

        return callsToSave
    }

    /**
     * Get Calls To Remove
     */
    private fun getCallsToRemove(
            callsSaved: List<CallDetailEntity>,
            callsRegistered: List<CallDetailEntity>
    ): List<CallDetailEntity> {

        val callsToRemove = arrayListOf<CallDetailEntity>()

        for(callSaved in callsSaved) {

            if(callSaved.remove == 1) {
                callsToRemove.add(callSaved)
                continue
            }

            var isFound = false
            for(callRegistered in callsRegistered){
                if(callSaved.id == callRegistered.id)
                    isFound = true
            }

            if(!isFound) {
                if(callSaved.sync == 1 && callSaved.serverId != null) {
                    callSaved.remove = 1
                    callsToRemove.add(callSaved)
                } else {
                    callDetailsRepositoryImpl.delete(callSaved)
                }
            }

        }

        return callsToRemove
    }

    /**
     * Get Calls To Synchronize
     */
    private fun getCallsToSynchronize(): Pair<List<CallDetailEntity>, List<CallDetailEntity>> {

        // Calls To Save
        val callsToSave = arrayListOf<CallDetailEntity>()
        // Calls To Remove
        val callsToRemove = arrayListOf<CallDetailEntity>()
        // Get Call Details
        val callsRegisteredInTheTerminal = getCallsRegisteredInTheTerminal()
        // Call Details Saved
        val callDetailsSaved = callDetailsRepositoryImpl.list()


        if(callsRegisteredInTheTerminal.isEmpty() && callDetailsSaved.isNotEmpty()) {

            // Delete sync call details
            callsToRemove.addAll(callDetailsSaved
                    .filter { it.sync == 1 && it.serverId != null }
                    .onEach { it.remove = 1 }
                    .map { it })

            // delete unsync call details
            callDetailsRepositoryImpl.delete(
                    callDetailsSaved
                            .filter { it.sync == 0 }
                            .map { it }
            )

        } else if(callsRegisteredInTheTerminal.isNotEmpty() && callDetailsSaved.isEmpty()) {

            // save all calls registered
            callsToSave.addAll(callsRegisteredInTheTerminal)

        } else if(callsRegisteredInTheTerminal.isNotEmpty() && callDetailsSaved.isNotEmpty()) {

            // Get Calls To Save
            callsToSave.addAll(getCallsToSave(callsRegisteredInTheTerminal, callDetailsSaved))
            // Get Calls To Remove
            callsToRemove.addAll(getCallsToRemove(callDetailsSaved, callsRegisteredInTheTerminal))

        }

        return Pair(callsToSave, callsToRemove)
    }

    /**
     * Upload Call Details
     */
    private suspend fun uploadCallDetails(callsToUpload: List<CallDetailEntity>): Int {
        Preconditions.checkNotNull(callsToUpload, "Calls To Upload can not be null")
        Preconditions.checkState(!callsToUpload.isEmpty(), "Calls To Upload can not be empty")

        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()

        val callsUploaded = arrayListOf<CallDetailEntity>()

        callsToUpload.asSequence().batch(BATCH_SIZE).forEach { group ->

            val response = callService
                    .saveHistoryCallsInTheTerminal(kid, terminal, group.map { SaveCallDetailDTO(it.phoneNumber,
                            it.callDayTime, it.callDuration, it.callType, it.id, kid, terminal) })
                    .await()

            response.httpStatus?.let {

                if(it == "OK") {
                    response.data?.forEach {callDetailDTO ->
                        group.map {
                            if(it.id == callDetailDTO.localId) {
                                it.serverId = callDetailDTO.identity
                                it.sync = 1
                                it.remove = 0
                            }
                        }
                    }
                    // Save Sync Calls
                    callDetailsRepositoryImpl.save(group)
                    // Add To List
                    callsUploaded.addAll(group)
                } else {
                    Timber.d("No Success Sync SMS")
                }

            }
        }

        return callsUploaded.size

    }

    /**
     * Delete Call Details
     */
    private suspend fun deleteCallDetails(callsToRemove: List<CallDetailEntity>): Int {
        Preconditions.checkNotNull(callsToRemove, "Calls To Remove can not be null")
        Preconditions.checkState(!callsToRemove.isEmpty(), "Calls To remove can not be empty")

        val kidId = preferenceRepository.getPrefKidIdentity()
        val terminalId = preferenceRepository.getPrefTerminalIdentity()

        var totalCallsDeleted  = 0

        val response = callService.deleteCallDetailsFromTerminal(
                kidId, terminalId, callsToRemove.filter { it.sync == 1 && it.serverId != null }
                .map { it.serverId!! }
        ).await()

        response.httpStatus?.let {

            if (it == "OK") {
                totalCallsDeleted = callsToRemove.size
                // save all as removed = true
                callsToRemove.onEach { it.remove = 1 }
                callDetailsRepositoryImpl.save(callsToRemove)
                callDetailsRepositoryImpl.delete(callsToRemove)
            }

        }

        return totalCallsDeleted

    }



    /**
     * On Execute
     */
    override suspend fun onExecuted(params: None): Int {

        // Get Calls To Sync
        val callsToSync = getCallsToSynchronize()
        val callsToUpload = callsToSync.first
        val callsToRemove = callsToSync.second

        var totalCallsSync = 0

        // Calls To Upload
        if(callsToUpload.isNotEmpty()) {
            // Save Call Details
            callDetailsRepositoryImpl.save(callsToUpload)
            Timber.d("%s Calls To Sync -> %s", TAG, callsToUpload.size)
            // Upload Call Details
            totalCallsSync += uploadCallDetails(callsToUpload)
        }

        // Remove Calls
        if(callsToRemove.isNotEmpty()) {
            // Save Call Details
            callDetailsRepositoryImpl.save(callsToRemove)
            Timber.d("%s Calls To Remove -> %s", TAG, callsToRemove.size)
            totalCallsSync += deleteCallDetails(callsToRemove)
        }

        return totalCallsSync
    }
}