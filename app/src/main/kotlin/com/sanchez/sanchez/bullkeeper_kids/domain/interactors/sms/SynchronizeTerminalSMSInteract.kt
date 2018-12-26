package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.sms

import android.content.Context
import android.net.Uri
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.extension.batch
import com.sanchez.sanchez.bullkeeper_kids.core.extension.toDateTimeString
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.SmsEntity
import retrofit2.Retrofit
import javax.inject.Inject
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveSmsDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ISmsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.SmsRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import timber.log.Timber

/**
 * Synchronize Terminal SMS Interact
 */
class SynchronizeTerminalSMSInteract
    @Inject constructor(
            private val context: Context,
            private val smsService: ISmsService,
            private val smsRepositoryImpl: SmsRepositoryImpl,
            private val preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit): UseCase<Int, UseCase.None>(retrofit) {

    val TAG = "SYNC_TERMINAL_SMS"

    companion object {
        val BATCH_SIZE = 5
    }

    /**
     * Get Registered SMS
     */
    private fun getRegisteredSMS(): List<SmsEntity> {
        val lstSms = ArrayList<SmsEntity>()
        val message = Uri.parse("content://sms/")
        val cr = context.contentResolver
        val c = cr.query(message, null, null, null, null)
        val totalSMS = c.count
        if (c.moveToFirst()) {
            for (i in 0 until totalSMS) {

                val objSms = SmsEntity()
                // Set Id
                objSms.id  = c.getString(c.getColumnIndexOrThrow("_id"))
                // Set Address
                objSms.address = c!!.getString(c
                        .getColumnIndexOrThrow("address"))
                // Set Message
                objSms.message = c.getString(c.getColumnIndexOrThrow("body"))
                // Set Read State
                val readState = c.getString(c.getColumnIndex("read"))

                objSms.readState = when(readState) {
                    "0" -> "NO_VIEWED"
                    "1" -> "VIEWED"
                    else -> "UNKNOWN"
                }

                // Set Time
                val time = c.getString(c.getColumnIndexOrThrow("date"))
                // Save Date Time
                objSms.time = time.toLong().toDateTimeString()

                if (c.getString(c.getColumnIndexOrThrow("type"))
                                .contains("1")) {
                    objSms.folderName = "INBOX"
                } else {
                    objSms.folderName = "SENT"
                }

                lstSms.add(objSms)
                c.moveToNext()
            }
        }

        c.close()

        return lstSms
    }

    /**
     * Get SMS To Save
     */
    private fun getSmsToSave(smsRegisteredList: List<SmsEntity>, smsSavedList: List<SmsEntity>) :
            List<SmsEntity>{
        // SMS to upload
        val smsToUpload = arrayListOf<SmsEntity>()

        for(smsRegistered in smsRegisteredList) {
            var isFound = false
            for(smsSaved in smsSavedList) {
                if(smsRegistered.id == smsSaved.id) {
                    isFound = true
                    if (smsSaved.sync == 0 || smsSaved.remove == 1) {
                        smsSaved.remove = 0
                        smsToUpload.add(smsSaved)
                    }
                }
            }
            if(!isFound)
                smsToUpload.add(smsRegistered)
        }
        return smsToUpload
    }

    /**
     * Get SMS To Remove
     */
    private fun getSmsToRemove(smsSavedList: List<SmsEntity>, smsRegisteredList: List<SmsEntity>):
            List<SmsEntity>{
        // SMS to remove
        val smsToRemove = arrayListOf<SmsEntity>()

        for(smsSaved in smsSavedList) {

            if(smsSaved.remove == 1) {
                smsToRemove.add(smsSaved)
                continue
            }

            var isFound = false
            for(smsRegistered in smsRegisteredList) {
                if(smsSaved.id == smsRegistered.id)
                    isFound = true
            }

            if(!isFound) {
                if(smsSaved.sync == 1 && smsSaved.serverId != null)
                    smsToRemove.add(smsSaved)
                else
                    smsRepositoryImpl.delete(smsSaved)
            }
        }

        return smsToRemove
    }

    /**
     * Get Sms To Synchronize
     */
    private fun getSmsToSynchronize(): Pair<List<SmsEntity>, List<SmsEntity>>{

        // Sms To Save
        val smsToSave = arrayListOf<SmsEntity>()
        // Sms To Remove
        val smsToRemove = arrayListOf<SmsEntity>()
        // Registered SMS
        val registeredSms = getRegisteredSMS()
        Timber.d("%s - Registered SMS, total -> %d", TAG, registeredSms.size)
        // Sms Saved
        val smsSaved = smsRepositoryImpl.list()
        Timber.d("%s - SMS Saved, total -> %d", TAG, smsSaved.size)

        if (registeredSms.isEmpty() && smsSaved.isNotEmpty()) {
            // Check to remove synchronized sms
            smsToRemove.addAll(smsSaved
                    .filter { it.sync == 1 && it.serverId != null }
                    .onEach { it.remove = 1 }
                    .map { it })
            // Clear unsynchronized sms
            smsRepositoryImpl.delete(smsSaved
                    .filter { it.sync == 0 }
                    .map { it })
        } else if(registeredSms.isNotEmpty() && smsSaved.isEmpty()) {
            // Sync all SMS
            smsToSave.addAll(registeredSms)
        } else if(registeredSms.isNotEmpty() && smsSaved.isNotEmpty()) {
            // Get All SMS to save
            smsToSave.addAll(getSmsToSave(registeredSms, smsSaved))
            // Get All SMS to remove
            smsToRemove.addAll(getSmsToRemove(smsSaved, registeredSms))
        }

        return Pair(smsToSave, smsToRemove)
    }

    /**
     * Upload SMS to sync
     */
    private suspend fun uploadSmsToSync(smsToSync: List<SmsEntity>): Int {
        Preconditions.checkNotNull(smsToSync, "Sms to sync can not be null")
        Preconditions.checkState(!smsToSync.isEmpty(), "Sms to sync can not be empty")

        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()

        val smsUploaded = arrayListOf<SmsEntity>()

        smsToSync.asSequence().batch(BATCH_SIZE).forEach { group ->

            val response = smsService
                    .saveSmsInTheTerminal(kid, terminal, group.map { SaveSmsDTO(it.address,
                            it.message, it.readState, it.time, it.folderName, it.id,
                            kid, terminal) })
                    .await()

            response.httpStatus?.let {

                if(it == "OK") {

                    response.data?.forEach {smsDTO ->
                        group.map {
                            if(it.id == smsDTO.localId) {
                                it.serverId = smsDTO.identity
                                it.sync = 1
                            }
                        }
                    }

                    // Save Sync SMS
                    smsRepositoryImpl.save(group)
                    // Add To List
                    smsUploaded.addAll(group)
                } else {
                    Timber.d("No Success Sync SMS")
                }

            }
        }

        return smsUploaded.size

    }

    /**
     * Remove SMS
     */
    private suspend fun removeSms(smsToRemove: List<SmsEntity>): Int{
        Preconditions.checkNotNull(smsToRemove, "Sms to remove can not be null")
        Preconditions.checkState(!smsToRemove.isEmpty(), "Sms to remove can not be empty")

        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()

        var smsRemoved = 0

        val response = smsService.deleteSmsFromTerminal(
                kid, terminal, smsToRemove.filter { it.sync == 1 && it.serverId != null }
                .map { it.serverId!! }
        ).await()

        response.httpStatus?.let {

            if (it == "OK") {
                smsRemoved = smsToRemove.size
                // save all as removed = true
                smsToRemove.onEach { it.remove = 1 }
                smsRepositoryImpl.save(smsToRemove)
                smsRepositoryImpl.delete(smsToRemove)
            }

        }

        return smsRemoved

    }

    /**
     * On Execute
     */
    override suspend fun onExecuted(params: None): Int {

        val smsToSync = getSmsToSynchronize()
        val smsToUpload = smsToSync.first
        val smsToRemove = smsToSync.second

        var totalSmsSync = 0

        // Upload Sms
        if(smsToUpload.isNotEmpty()) {
            smsRepositoryImpl.save(smsToUpload)
            Timber.d("SMS to upload -> %d", smsToUpload.size)
            totalSmsSync += uploadSmsToSync(smsToUpload)
        }

        // Remove Sms
        if(smsToRemove.isNotEmpty()) {
            smsRepositoryImpl.save(smsToRemove)
            Timber.d("SMS to remove -> %d", smsToRemove.size)
            totalSmsSync += removeSms(smsToRemove)
        }

        return totalSmsSync
    }
}