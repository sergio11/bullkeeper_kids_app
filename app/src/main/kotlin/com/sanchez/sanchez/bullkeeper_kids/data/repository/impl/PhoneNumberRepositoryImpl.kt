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

    /**
     * Find By Phone Number
     */
    override fun findByPhoneNumber(phoneNumber: String): PhoneNumberBlockedEntity? {
        Timber.d("Find Phone Number blocked as $phoneNumber")
        val realm = Realm.getDefaultInstance()
        val realmResult = realm.where(PhoneNumberBlockedEntity::class.java)
                .equalTo("phoneNumber", phoneNumber).findFirst()
        var phoneNumberBlocked: PhoneNumberBlockedEntity? = null
        if(realmResult != null)
            phoneNumberBlocked = realm.copyFromRealm(realmResult)
        realm.close()
        return phoneNumberBlocked
    }


    /**
     * Delete By Phone Number or Id
     */
    override fun deleteByPhoneNumberOrId(idOrPhoneNumber: String) {
        val realm = Realm.getDefaultInstance()
        val phoneNumberBlockedToDelete =
                realm.where(PhoneNumberBlockedEntity::class.java)
                        .equalTo("identity", idOrPhoneNumber)
                        .or().equalTo("phoneNumber", idOrPhoneNumber)
                        .findAll()
        realm.executeTransaction {
            phoneNumberBlockedToDelete.deleteAllFromRealm()
        }
        realm.close()
    }

}