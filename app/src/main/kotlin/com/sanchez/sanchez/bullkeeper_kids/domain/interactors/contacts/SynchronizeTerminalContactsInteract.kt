package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.contacts

import android.annotation.SuppressLint
import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import retrofit2.Retrofit
import javax.inject.Inject
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IContactsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.ContactRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import android.provider.ContactsContract
import com.sanchez.sanchez.bullkeeper_kids.data.entity.ContactEntity
import android.graphics.BitmapFactory
import android.content.ContentUris
import android.util.Base64
import java.io.IOException
import android.graphics.Bitmap
import com.sanchez.sanchez.bullkeeper_kids.core.extension.batch
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveContactDTO
import timber.log.Timber
import java.io.ByteArrayOutputStream

/**
 * Synchronize Terminal Contacts Interact
 */
class SynchronizeTerminalContactsInteract
    @Inject constructor(
            private val context: Context,
            private val contactsService: IContactsService,
            private val contactRepositoryImpl: ContactRepositoryImpl,
            private val preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit): UseCase<Int, UseCase.None>(retrofit) {

    companion object {
        val BATCH_SIZE = 5
    }

    /**
     * Get Contact List
     */
    @SuppressLint("Recycle")
    private fun getContactList(): List<ContactEntity> {

        val contactList = arrayListOf<ContactEntity>()

        val cr = context.contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {

                val id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID))

                val name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME))

                var photo: String? = null

                try {
                    val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.contentResolver,
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id.toLong()))

                    if (inputStream != null) {
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val byteArray = stream.toByteArray()
                        bitmap.recycle()
                        photo = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        inputStream.close()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }


                if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                    val pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf<String>(id), null)
                    while (pCur!!.moveToNext()) {
                        val phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER))

                        val contactEntity = ContactEntity()
                        contactEntity.id = id
                        contactEntity.name = name
                        contactEntity.phoneNumber = phoneNo
                        contactEntity.photoEncodedString = photo
                        contactList.add(contactEntity)
                    }
                    pCur.close()
                }
            }
        }

        cur?.close()

        return contactList
    }


    /**
     * On Execute
     */
    override suspend fun onExecuted(params: None): Int {

        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()

        // Get Contact List
        val contactList = getContactList()

        var totalContactsSync = 0

        if(contactList.isNotEmpty()) {
            contactRepositoryImpl.save(contactList)
            Timber.d("Total contacts saved -> %d", contactList.size)

            val contactSyncList = arrayListOf<ContactEntity>()

            contactList.asSequence().batch(BATCH_SIZE).forEach { group ->

                val response = contactsService
                        .saveContactsFromTerminal(kid, terminal, group.map {
                            SaveContactDTO(it.name, it.phoneNumber, it.id, it.photoEncodedString,
                                    kid, terminal)
                        })
                        .await()

                response.httpStatus?.let {

                    if(it == "OK") {

                        response.data?.forEach {contactDTO ->
                            group.map {
                                if(it.id == contactDTO.localId) {
                                    it.serverId = contactDTO.identity
                                    it.sync = 1
                                }
                            }
                        }
                        // Save Sync Contacts
                        contactRepositoryImpl.save(group)
                        // Add To List
                        contactSyncList.addAll(group)
                    }

                }

            }

            totalContactsSync = contactSyncList.size

        }

        return totalContactsSync
    }
}