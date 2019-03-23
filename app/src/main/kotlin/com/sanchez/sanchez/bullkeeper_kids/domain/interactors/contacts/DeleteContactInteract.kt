package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.contacts

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Delete Contact Interact
 */
class DeleteContactInteract
    @Inject constructor(
            private val context: Context,
            retrofit: Retrofit): UseCase<Unit, DeleteContactInteract.Params>(retrofit) {

    /**
     *
     */
    override suspend fun onExecuted(params: Params) {

        val cr = context.contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null)
        cur?.let {
            try {
                if (it.moveToFirst()) {
                    do {
                        if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup._ID)) == params.contactId) {
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
     * Params
     */
    data class Params(
            val contactId: String
    )
}