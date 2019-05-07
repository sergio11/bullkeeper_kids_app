package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppAllowedByScheduledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppAllowedByScheduledRepository
import io.realm.Realm
import timber.log.Timber

/**
 * App Allowed By Scheduled Repository Impl
 */
class AppAllowedByScheduledRepositoryImpl: SupportRepositoryImpl<AppAllowedByScheduledEntity>(),
    IAppAllowedByScheduledRepository{

    /**
     * Delete
     */
    override fun delete(model: AppAllowedByScheduledEntity) {
        Timber.d("Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        val appAllowedByScheduled = realm.where(AppAllowedByScheduledEntity::class.java)
                .equalTo("identity", model.identity)
                .findFirst()
        // Remove model into writable transaction
        realm.executeTransaction {
            appAllowedByScheduled?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Delete
     */
    override fun delete(modelList: List<AppAllowedByScheduledEntity>) {
        for(model in modelList) delete(model)
    }

    /**
     * Delete All
     */
    override fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.delete(AppAllowedByScheduledEntity::class.java)
        }
        realm.close()
    }

    /**
     * List
     */
    override fun list(): List<AppAllowedByScheduledEntity> {
        val realm = Realm.getDefaultInstance()
        val appsAllowedList =
                realm.copyFromRealm(
                        realm.where(AppAllowedByScheduledEntity::class.java).findAll())
        realm.close()
        return appsAllowedList
    }

    /**
     * Find By App And Terminal And Scheduled Block
     */
    override fun isAppAllowed(app: String, scheduledBlock: String): Boolean {
        val realm = Realm.getDefaultInstance()
        val total = realm.where(AppAllowedByScheduledEntity::class.java)
                .equalTo("app", app)
                .equalTo("scheduledBlock", scheduledBlock)
                .count()
        realm.close()
        return total > 0
    }

    /**
     * Any App Allowed
     */
    override fun anyAppAllowed(scheduledBlock: String): Boolean {
        val realm = Realm.getDefaultInstance()
        val totalAppsAllowed = realm.where(AppAllowedByScheduledEntity::class.java)
                .equalTo("scheduledBlock", scheduledBlock)
                .count()
        realm.close()
        return totalAppsAllowed > 0
    }

    /**
     * Delete By Scheduled Block Id
     */
    override fun deleteByScheduledBlockId(id: String) {
        val realm = Realm.getDefaultInstance()
        val appAllowedList = realm.where(AppAllowedByScheduledEntity::class.java)
                .equalTo("scheduledBlock", id)
                .findAll()
        realm.executeTransaction {
            appAllowedList?.deleteAllFromRealm()
        }
        realm.close()
    }

}