package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.entity.ContactEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IContactRepository
import io.realm.Realm
import timber.log.Timber

/**
 * Contact Repository
 */
class ContactRepositoryImpl: SupportRepositoryImpl<ContactEntity>(), IContactRepository {

    /**
     * Delete
     */
    override fun delete(model: ContactEntity) {
        Timber.d("Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        // Find Contact Detail
        val contactDetail = realm.where(ContactEntity::class.java)
                .equalTo("id", model.id)
                .findFirst()
        realm.executeTransaction {
            contactDetail?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Delete
     */
    override fun delete(modelList: List<ContactEntity>) {
        for(model in modelList) delete(model)
    }

    /**
     * List
     */
    override fun list(): List<ContactEntity> {
        val realm = Realm.getDefaultInstance()
        val contactList =
                realm.copyFromRealm(
                        realm.where(ContactEntity::class.java).findAll())
        realm.close()
        return contactList
    }
}