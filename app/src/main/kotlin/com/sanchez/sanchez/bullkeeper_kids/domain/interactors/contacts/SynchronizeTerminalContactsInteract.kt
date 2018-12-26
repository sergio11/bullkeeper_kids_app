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
import com.fernandocejas.arrow.checks.Preconditions
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


    val TAG = "SYNC_TERMINAL_CONTACTS"

    companion object {
        val BATCH_SIZE = 5
    }

    /**
     * Get Contact List
     */
    @SuppressLint("Recycle")
    private fun getContactsRegisteredInTheTerminal(): List<ContactEntity> {

        val contactList = arrayListOf<ContactEntity>()

        val cr = context.contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {

                val id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID))

                val name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME))

                val lastUpdateTimestamp =
                        cur.getString(
                                cur.getColumnIndex(
                                        ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP)
                        )

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
                        contactEntity.lastUpdateTimestamp = lastUpdateTimestamp
                        contactList.add(contactEntity)
                    }
                    pCur.close()
                }
            }
        }

        cur?.close()

        return contactList.distinctBy { it.id }
    }

    /**
     * Get Contact To Save
     */
    private fun getContactsToSave(contactsRegistered: List<ContactEntity>, contactsSaved: List<ContactEntity>)
            : List<ContactEntity> {
        // Contacts To Save
        val contactsToSave = arrayListOf<ContactEntity>()
        // Get Contacts to save
        for (contactRegistered in contactsRegistered) {
            var isFound = false
            for (contactSaved in contactsSaved) {
                if (contactRegistered.id == contactSaved.id) {
                    // Contact found
                    isFound = true

                    if (contactRegistered.getLastUpdateAsDate() != null && contactSaved.getLastUpdateAsDate() != null &&
                            contactRegistered.getLastUpdateAsDate()!!.after(contactSaved.getLastUpdateAsDate())) {
                        // Contact Required Updated
                        contactRegistered.serverId = contactSaved.serverId
                        contactRegistered.sync = 0
                        contactRegistered.remove = 0
                        contactsToSave.add(contactRegistered)
                    } else {

                        // Is Contact Save Sync?
                        if (contactSaved.sync == 0 || contactSaved.remove == 1) {
                            contactSaved.remove = 0
                            contactsToSave.add(contactSaved)
                        }

                    }
                }
            }
            // is contact found? add to save
            if(!isFound)
                contactsToSave.add(contactRegistered)
        }

        return contactsToSave

    }

    /**
     * Get Contacts to Remove
     */
    private fun getContactsToRemove(contactsSaved: List<ContactEntity>, contactsRegistered: List<ContactEntity>)
        : List<ContactEntity> {

        val contactsToRemove = arrayListOf<ContactEntity>()

        for(contactSaved in contactsSaved) {

            if(contactSaved.remove == 1) {
                contactsToRemove.add(contactSaved)
                continue
            }

            var isFound = false
            for(contactRegistered in contactsRegistered) {
                if(contactSaved.id == contactRegistered.id)
                    isFound = true
            }

            if(!isFound) {
                if (contactSaved.sync == 1 && contactSaved.serverId != null) {
                    contactSaved.remove = 1
                    contactsToRemove.add(contactSaved)
                } else {
                    contactRepositoryImpl.delete(contactSaved)
                }
            }
        }

        return contactsToRemove
    }

    /**
     * Get Contacts To Sync
     */
    private fun getContactsToSynchronize(): Pair<List<ContactEntity>, List<ContactEntity>> {
        // Contacts To Save
        val contactsToSave = arrayListOf<ContactEntity>()
        // Contacts To Remove
        val contactsToRemove = arrayListOf<ContactEntity>()
        // Get Contacts Registered
        val contactsRegistered = getContactsRegisteredInTheTerminal()
        // Get Contacts Saved
        val contactsSaved = contactRepositoryImpl.list()

        if(contactsRegistered.isEmpty() &&  contactsSaved.isNotEmpty()) {
            // Delete All Contact
            contactsToRemove.addAll(contactsSaved
                    .filter { it.sync == 1 && it.serverId != null }
                    .onEach { it.remove = 1 }
                    .map { it })
            // Delete unsync contacts
            contactRepositoryImpl.delete(contactsSaved
                    .filter { it.sync == 0 }
                    .map { it })

        } else if(contactsRegistered.isNotEmpty() && contactsSaved.isEmpty()) {
            // Sync All contacts registered
            contactsToSave.addAll(contactsRegistered)

        } else if(contactsRegistered.isNotEmpty() && contactsSaved.isNotEmpty()) {

            // Get Contact To Save
            contactsToSave.addAll(getContactsToSave(contactsRegistered, contactsSaved))
            // Get Contact to remove
            contactsToRemove.addAll(getContactsToRemove(contactsSaved, contactsRegistered))

        }

        return Pair(contactsToSave, contactsToRemove)

    }

    /**
     * Upload Contacts To Sync
     */
    private suspend fun uploadContactsToSync(contactsToSync: List<ContactEntity>): Int {
        Preconditions.checkNotNull(contactsToSync, "Contacts to sync can not be null")
        Preconditions.checkState(!contactsToSync.isEmpty(), "Contacts to sync can not be empty")

        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()

        val contactsUpload = arrayListOf<ContactEntity>()

        contactsToSync.asSequence().batch(BATCH_SIZE).forEach { group ->

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
                    contactsUpload.addAll(group)
                }

            }
        }

        return contactsUpload.size
    }

    /**
     * Delete Contacts
     */
    private suspend fun deleteContacts(contactsToDelete: List<ContactEntity>): Int{
        Preconditions.checkNotNull(contactsToDelete, "Contacts to delete can not be null")
        Preconditions.checkState(!contactsToDelete.isEmpty(), "Contacts to delete can not be empty")

        val kid = preferenceRepository.getPrefKidIdentity()
        val terminal = preferenceRepository.getPrefTerminalIdentity()

        var totalContactsDeleted  = 0

        val response = contactsService.deleteContactsFromTerminal(
                kid, terminal, contactsToDelete.filter { it.sync == 1 && it.serverId != null }
                .map { it.serverId!! }
        ).await()

        response.httpStatus?.let {

            if (it == "OK") {
                totalContactsDeleted = contactsToDelete.size
                // save all as removed = true
                contactsToDelete.onEach { it.remove = 1 }
                contactRepositoryImpl.save(contactsToDelete)
                contactRepositoryImpl.delete(contactsToDelete)
            }

        }

        return totalContactsDeleted

    }


    /**
     * On Execute
     */
    override suspend fun onExecuted(params: None): Int {

        // Get Contact List
        val contactsToSync = getContactsToSynchronize()

        val contactToUpload = contactsToSync.first
        val contactToRemove = contactsToSync.second

        var totalContactsSync = 0

        // Upload Contacts
        if(contactToUpload.isNotEmpty()) {
            contactRepositoryImpl.save(contactToUpload)
            Timber.d("$TAG Total contacts to upload -> %d", contactToUpload.size)
            totalContactsSync += uploadContactsToSync(contactToUpload)
        }

        // Delete Contacts
        if (contactToRemove.isNotEmpty()) {
            contactRepositoryImpl.save(contactToRemove)
            Timber.d("$TAG Total contacts to remove -> %d", contactToRemove.size)
            totalContactsSync += deleteContacts(contactToRemove)
        }

        return totalContactsSync
    }
}