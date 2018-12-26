package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPhoneNumberRepository
import com.sanchez.sanchez.bullkeeper_kids.data.entity.PhoneNumberBlockedEntity
import io.realm.Realm
import timber.log.Timber

/**
 * Phone Number Repository Impl
 */
class PhoneNumberRepositoryImpl: SupportRepositoryImpl<PhoneNumberBlockedEntity>(), IPhoneNumberRepository {


    /**
     * Delete
     */
    override fun delete(model: PhoneNumberBlockedEntity) {
        Timber.d("Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        val phoneNumberBlocked = realm.where(PhoneNumberBlockedEntity::class.java)
                .equalTo("id", model.identity)
                .findFirst()
        realm.executeTransaction {
            phoneNumberBlocked?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Delete
     */
    override fun delete(modelList: List<PhoneNumberBlockedEntity>) {
        for(model in modelList) delete(model)
    }

    /**
     * Delete All
     */
    override fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.delete(PhoneNumberBlockedEntity::class.java)
        }
        realm.close()
    }

    /**
     * List
     */
    override fun list(): List<PhoneNumberBlockedEntity> {
        val realm = Realm.getDefaultInstance()
        val phoneNumbersBlockedList =
                realm.copyFromRealm(
                        realm.where(PhoneNumberBlockedEntity::class.java).findAll())
        realm.close()
        return phoneNumbersBlockedList
    }
}