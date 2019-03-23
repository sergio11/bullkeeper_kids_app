package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.contacts

import android.annotation.SuppressLint
import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import retrofit2.Retrofit
import javax.inject.Inject
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IContactsService
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
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IContactRepository
import timber.log.Timber
import java.io.ByteArrayOutputStream
import android.net.Uri
import android.provider.ContactsContract.PhoneLookup




/**
 * Synchronize Terminal Contacts Interact
 */
class SynchronizeTerminalContactsInteract
    @Inject constructor(
            private val context: Context,
            private val contactsService: IContactsService,
            private val contactRepositoryImpl: IContactRepository,
            private val preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit): UseCase<Int, UseCase.None>(retrofit) {


    val TAG = "SYNC_TERMINAL_CONTACTS"

    companion object {
        val BATCH_SIZE = 15
    }

    inner class ContactItem {

        var id: String? = null
        var displayName: String? = null
        var photoEncodedString: String? = null
        var lastUpdateTimestamp: String? = null
        var arrayListPhone: ArrayList<PhoneContact> = ArrayList()
        var arrayListEmail: ArrayList<EmailContact> = ArrayList()
        var arrayListAddress: ArrayList<PostalAddress> = ArrayList()
    }

    inner class EmailContact {
        var email = ""
    }

    inner class PhoneContact {
        var phone = ""
    }


    inner class PostalAddress {
        var city = ""
        var state = ""
        var country = ""
    }


    private fun getReadContacts(): ArrayList<ContactItem> {
        val contactList = ArrayList<ContactItem>()
        val cr = context.contentResolver
        val mainCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (mainCursor != null) {
            while (mainCursor.moveToNext()) {
                val contactItem = ContactItem()
                val id = mainCursor.getString(
                        mainCursor.getColumnIndex(ContactsContract.Contacts._ID))
                val displayName = mainCursor.getString(
                        mainCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val lastUpdateTimestamp =
                        mainCursor.getString(
                                mainCursor.getColumnIndex(
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
                //ADD NAME AND CONTACT PHOTO DATA...
                contactItem.displayName = displayName
                contactItem.photoEncodedString = photo
                contactItem.lastUpdateTimestamp = lastUpdateTimestamp
                contactItem.id = id

                if (Integer.parseInt(mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    //ADD PHONE DATA...
                    val arrayListPhone = ArrayList<PhoneContact>()
                    val phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf<String>(id), null)
                    if (phoneCursor != null) {
                        while (phoneCursor.moveToNext()) {
                            val phoneContact = PhoneContact()
                            val phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            phoneContact.phone = phone
                            arrayListPhone.add(phoneContact)
                        }
                    }
                    phoneCursor?.close()
                    contactItem.arrayListPhone = arrayListPhone


                    //ADD E-MAIL DATA...
                    val arrayListEmail = ArrayList<EmailContact>()
                    val emailCursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", arrayOf<String>(id), null)
                    if (emailCursor != null) {
                        while (emailCursor.moveToNext()) {
                            val emailContact = EmailContact()
                            val email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                            emailContact.email = email
                            arrayListEmail.add(emailContact)
                        }
                    }
                    emailCursor?.close()
                    contactItem.arrayListEmail = arrayListEmail

                    //ADD ADDRESS DATA...
                    val arrayListAddress = ArrayList<PostalAddress>()
                    val addrCursor = context.contentResolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?", arrayOf<String>(id), null)
                    if (addrCursor != null) {
                        while (addrCursor.moveToNext()) {
                            val postalAddress = PostalAddress()
                            val city = addrCursor.getString(addrCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY))
                            val state = addrCursor.getString(addrCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION))
                            val country = addrCursor.getString(addrCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY))
                            postalAddress.city = city
                            postalAddress.state = state
                            postalAddress.country = country
                            arrayListAddress.add(postalAddress)
                        }
                    }
                    addrCursor?.close()
                    contactItem.arrayListAddress = arrayListAddress
                }
                contactList.add(contactItem)
            }
        }
        mainCursor?.close()
        return contactList
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
                        Timber.d("Contacts: Add Contact to save -> %s - %s - %s - %d ",
                                contactRegistered.name, contactRegistered.serverId, contactRegistered.id,
                                contactRegistered.sync)
                        contactsToSave.add(contactRegistered)
                    } else {

                        // Is Contact Save Sync?
                        if (contactSaved.sync == 0 || contactSaved.remove == 1) {
                            contactSaved.remove = 0
                            Timber.d("Contacts: Add Contact to save -> %s - %s - %s - %d ",
                                    contactSaved.name, contactSaved.serverId, contactSaved.id,
                                    contactSaved.sync)
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
                Timber.d("Contacts: Contact to remove (contactSaved.remove == 1) -> %s - %s - %s - %d ",
                        contactSaved.name, contactSaved.serverId, contactSaved.id,
                        contactSaved.sync)
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
                    Timber.d("Contacts: Add Contact to remove (!isFound && contactSaved.sync == 1 && contactSaved.serverId != null) -> %s - %s - %s - %d ",
                            contactSaved.name, contactSaved.serverId, contactSaved.id,
                            contactSaved.sync)
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
                                Timber.d("Contacts: Contact Sync: %s - %s",
                                        it.name, it.serverId)
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

        val newContactList = getReadContacts()
        Timber.d("$TAG New Contact List size-> %d", newContactList.size)

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