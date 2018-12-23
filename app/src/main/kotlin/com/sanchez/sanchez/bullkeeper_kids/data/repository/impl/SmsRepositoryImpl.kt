package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.entity.SmsEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.ISmsRepository
import io.realm.Realm
import timber.log.Timber

/**
 * Sms Repository
 */
class SmsRepositoryImpl: SupportRepositoryImpl<SmsEntity>(), ISmsRepository {


    /**
     * Delete
     */
    override fun delete(model: SmsEntity) {
        Timber.d("Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        // Find Contact Detail
        val contactDetail = realm.where(SmsEntity::class.java)
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
    override fun delete(modelList: List<SmsEntity>) {
        for(model in modelList) delete(model)
    }

    /**
     * List
     */
    override fun list(): List<SmsEntity> {
        val realm = Realm.getDefaultInstance()
        val smsList =
                realm.copyFromRealm(
                        realm.where(SmsEntity::class.java).findAll())
        realm.close()
        return smsList
    }
}