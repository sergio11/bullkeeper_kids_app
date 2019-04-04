package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.contacts

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IContactsService
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Delete Disable Contact Interact
 */
class DeleteDisableContactInteract
    @Inject constructor(
            private val context: Context,
            private val contactService: IContactsService,
            private val preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit): UseCase<Unit, DeleteDisableContactInteract.Params>(retrofit) {

    /**
     * Delete Contacts
     * @param contactIds
     */
    private fun deleteContacts(contactIds: List<String>){

        val cr = context.contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null)
        cur?.let {
            try {
                if (it.moveToFirst()) {
                    do {
                        if (contactIds.contains(cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup._ID)))) {
                            val lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                            val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey)
                            cr.delete(uri, null, null)
                            break
                        }

                    } while (it.moveToNext())
                }

            } catch (e: Exception) {
                println(e.stackTrace)
            } finally {
                it.close()
            }
        }
    }


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: Params) {

        if(!params.contactIds.isNullOrEmpty()) {
            deleteContacts(params.contactIds)
        } else {

            if(preferenceRepository.getPrefKidIdentity()
                    != IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE &&
                    preferenceRepository.getPrefTerminalIdentity()
                        != IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE) {

                val response = contactService.getListOfDisabledContactsInTheTerminal(
                        preferenceRepository.getPrefKidIdentity(),
                        preferenceRepository.getPrefTerminalIdentity()).await()

                response.httpStatus?.let {
                    if (it == "OK") {
                        response.data?.filter { !it.localId.isNullOrEmpty() }
                                ?.map { it.localId!! }?.let {
                                    deleteContacts(it)
                                }
                    }
                }

            }


        }

    }

    /**
     * Params
     */
    data class Params(
            val contactIds: List<String>? = null
    )
}