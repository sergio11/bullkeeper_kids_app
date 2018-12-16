package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.sms

import android.app.Activity
import android.net.Uri
import com.sanchez.sanchez.bullkeeper_kids.core.extension.batch
import com.sanchez.sanchez.bullkeeper_kids.core.extension.toDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.SmsEntity
import retrofit2.Retrofit
import javax.inject.Inject
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveSmsDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.ISmsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.SmsRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import timber.log.Timber
import java.text.SimpleDateFormat

/**
 * Synchronize Terminal SMS Interact
 */
class SynchronizeTerminalSMSInteract
    @Inject constructor(
            private val activity: Activity,
            private val smsService: ISmsService,
            private val smsRepositoryImpl: SmsRepositoryImpl,
            private val preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit): UseCase<Int, UseCase.None>(retrofit) {

    companion object {
        val BATCH_SIZE = 5
    }

    /**
     * Get All Sms
     */
    private fun getAllSms(): List<SmsEntity> {
        val lstSms = ArrayList<SmsEntity>()
        val message = Uri.parse("content://sms/")
        val cr = activity.contentResolver
        val c = cr.query(message, null, null, null, null)
        activity.startManagingCursor(c)
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
                objSms.time = time.toLong().toDateTime()

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
     * On Execute
     */
    override suspend fun onExecuted(params: None): Int {

        val kidId = preferenceRepository.getPrefKidIdentity()
        val terminalId = preferenceRepository.getPrefTerminalIdentity()

        val smsList = getAllSms()

        var totalSmsSync = 0

        if(smsList.isNotEmpty()) {

            smsRepositoryImpl.save(smsList)

            Timber.d("%d sms saved", smsList.size)

            val smsSyncList = arrayListOf<SmsEntity>()

            smsList.asSequence().batch(BATCH_SIZE).forEach { group ->

                val response = smsService
                        .saveSmsInTheTerminal(kidId, terminalId, group.map { SaveSmsDTO(it.address,
                                it.message, it.readState, it.time, it.folderName, it.id, kidId, terminalId) })
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

                        // Save Sync Apps
                        smsRepositoryImpl.save(group)
                        // Add To List
                        smsSyncList.addAll(group)
                    } else {
                        Timber.d("No Success Sync SMS")
                    }

                }
            }

            totalSmsSync = smsSyncList.size

        }


        return totalSmsSync
    }
}