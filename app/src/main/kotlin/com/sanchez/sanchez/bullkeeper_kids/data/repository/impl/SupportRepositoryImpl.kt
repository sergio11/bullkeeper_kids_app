package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.repository.ISupportRepository
import io.realm.Realm
import io.realm.RealmObject
import timber.log.Timber

/**
 * Suport Repository Impl
 */
abstract class SupportRepositoryImpl<T: RealmObject>: ISupportRepository<T> {

    /**
     * Save
     */
    override fun save(model: T) {
        Timber.d("Save Model -> $model")
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.insertOrUpdate(model)
        }
        realm.close()
    }

    /**
     * Save
     */
    override fun save(modelList: List<T>) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.insertOrUpdate(modelList)
        }
        realm.close()
    }

}