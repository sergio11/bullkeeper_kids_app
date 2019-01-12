package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.data.entity.FunTimeDayScheduledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IFunTimeDayScheduledRepository
import io.realm.Realm
import timber.log.Timber

/**
 * Fun Time Day Scheduled Repository Impl
 */
class FunTimeDayScheduledRepositoryImpl: SupportRepositoryImpl<FunTimeDayScheduledEntity>(),
        IFunTimeDayScheduledRepository {

    /**
     * Delete
     */
    override fun delete(model: FunTimeDayScheduledEntity) {
        Timber.d("Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        // Find Fun Time Day Scheduled
        val funTimeDayScheduled = realm.where(FunTimeDayScheduledEntity::class.java)
                .equalTo("day", model.day)
                .findFirst()
        realm.executeTransaction {
            funTimeDayScheduled?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Delete
     */
    override fun delete(modelList: List<FunTimeDayScheduledEntity>) {
        for(model in modelList) delete(model)
    }

    /**
     * Delete All
     */
    override fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.delete(FunTimeDayScheduledEntity::class.java)
        }
        realm.close()
    }

    /**
     * List
     */
    override fun list(): List<FunTimeDayScheduledEntity> {
        val realm = Realm.getDefaultInstance()
        val funTimeDayScheduledList =
                realm.copyFromRealm(
                        realm.where(FunTimeDayScheduledEntity::class.java).findAll())
        realm.close()
        return funTimeDayScheduledList
    }

    /**
     * Get Fun Time Day Scheduled For Day
     */
    override fun getFunTimeDayScheduledForDay(dayName: String): FunTimeDayScheduledEntity? {
        Preconditions.checkNotNull(dayName, "Day Name can not be null")
        Preconditions.checkState(dayName.isEmpty(), "Day Name can not be empty")
        Timber.d("Fun Time by day name $dayName")
        val realm = Realm.getDefaultInstance()
        val realmResult = realm.where(FunTimeDayScheduledEntity::class.java)
                .equalTo("day", dayName).findFirst()
        var funTimeDayScheduled: FunTimeDayScheduledEntity? = null
        if(realmResult != null)
            funTimeDayScheduled = realm.copyFromRealm(realmResult)
        realm.close()
        return funTimeDayScheduled
    }

}