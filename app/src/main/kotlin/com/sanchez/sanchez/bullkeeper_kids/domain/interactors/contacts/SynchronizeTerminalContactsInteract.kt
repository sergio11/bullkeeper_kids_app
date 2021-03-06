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
import com.sanchez.sanchez.bullkeeper_kids.data.entity.EmailContact
import com.sanchez.sanchez.bullkeeper_kids.data.entity.PhoneContact
import com.sanchez.sanchez.bullkeeper_kids.data.entity.PostalAddress
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.EmailContactDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.PhoneContactDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.PostalAddressDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveContactDTO
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IContactRepository
import timber.log.Timber
import java.io.ByteArrayOutputStream




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

    /**
     * Get Contacts Registered In The Terminal
     */
    private fun getContactsRegisteredInTheTerminal(): ArrayList<ContactEntity> {
        val contactList = arrayListOf<ContactEntity>()
        val cr = context.contentResolver
        val mainCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (mainCursor != null) {
            while (mainCursor.moveToNext()) {
                val contactItem = ContactEntity()
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
                contactItem.name = displayName
                contactItem.photoEncodedString = photo
                contactItem.lastUpdateTimestamp = lastUpdateTimestamp
                contactItem.id = id

                if (Integer.parseInt(mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    //ADD PHONE DATA...
                    val arrayListPhone = ArrayList<PhoneContact>()
                    val phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf<String>(id), null)
                    if (phoneCursor != null) {
                        while (phoneCursor.moveToNext()) {
                            val phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            arrayListPhone.add(PhoneContact(phone))
                        }
                    }
                    phoneCursor?.close()
                    contactItem.phoneList = arrayListPhone


                    //ADD E-MAIL DATA...
                    val arrayListEmail = ArrayList<EmailContact>()
                    val emailCursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", arrayOf<String>(id), null)
                    if (emailCursor != null) {
                        while (emailCursor.moveToNext()) {
                            val email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                            arrayListEmail.add(EmailContact(email))
                        }
                    }
                    emailCursor?.close()
                    contactItem.emailList = arrayListEmail

                    //ADD ADDRESS DATA...
                    val arrayListAddress = ArrayList<PostalAddress>()
                    val addrCursor = context.contentResolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?", arrayOf<String>(id), null)
                    if (addrCursor != null) {
                        while (addrCursor.moveToNext()) {
                            val city = addrCursor.getString(addrCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY))
                            val state = addrCursor.getString(addrCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION))
                            val country = addrCursor.getString(addrCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY))
                            arrayListAddress.add( PostalAddress(city, state, country))
                        }
                    }
                    addrCursor?.close()
                    contactItem.addressList = arrayListAddress
                }
                contactList.add(contactItem)
            }
        }
        mainCursor?.close()
        return contactList
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
                        SaveContactDTO(it.name,  it.id, it.photoEncodedString,
                                it.phoneList.map { PhoneContactDTO(it.phone) }.toList(),
                                it.emailList.map { EmailContactDTO(it.email) }.toList(),
                                it.addressList.map { PostalAddressDTO(it.city, it.state, it.country) },
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