package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.entity.CallDetailEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.ICallDetailRepository
import io.realm.Realm
import timber.log.Timber

/**
 * Call Repository
 */
class CallDetailRepositoryImpl: SupportRepositoryImpl<CallDetailEntity>(), ICallDetailRepository {
    /**
     * Delete
     */
    override fun delete(model: CallDetailEntity) {
        Timber.d("Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        // Find Call Detail
        val callDetail = realm.where(CallDetailEntity::class.java)
                .equalTo("id", model.id)
                .findFirst()
        realm.executeTransaction {
            callDetail?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Delete
     */
    override fun delete(modelList: List<CallDetailEntity>) {
        for(model in modelList) delete(model)
    }

    /**
     * List
     */
    override fun list(): List<CallDetailEntity> {
        val realm = Realm.getDefaultInstance()
        val callDetailList =
                realm.copyFromRealm(
                        realm.where(CallDetailEntity::class.java).findAll())
        realm.close()
        return callDetailList
    }


}