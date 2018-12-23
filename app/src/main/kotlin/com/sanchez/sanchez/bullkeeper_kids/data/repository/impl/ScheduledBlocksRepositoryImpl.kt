package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.entity.ScheduledBlockEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IScheduledBlocksRepository
import io.realm.Realm
import timber.log.Timber

/**
 * Scheduled Blocks Repository
 */
class ScheduledBlocksRepositoryImpl: SupportRepositoryImpl<ScheduledBlockEntity>(),
    IScheduledBlocksRepository{

    /**
     * Delete
     */
    override fun delete(model: ScheduledBlockEntity) {
        Timber.d("Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        val scheduledBlock = realm.where(ScheduledBlockEntity::class.java)
                .equalTo("id", model.id)
                .findFirst()
        realm.executeTransaction {
            scheduledBlock?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Delete
     */
    override fun delete(modelList: List<ScheduledBlockEntity>) {
        for(model in modelList) delete(model)
    }

    /**
     * List
     */
    override fun list(): List<ScheduledBlockEntity> {
        val realm = Realm.getDefaultInstance()
        val scheduledBlockist =
                realm.copyFromRealm(
                        realm.where(ScheduledBlockEntity::class.java).findAll())
        realm.close()
        return scheduledBlockist
    }
}