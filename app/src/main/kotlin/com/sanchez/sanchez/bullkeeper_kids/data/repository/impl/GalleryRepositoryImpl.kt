package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.entity.GalleryImageEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGalleryRepository
import io.realm.Realm
import timber.log.Timber

/**
 * Gallery Repository Impl
 */
class GalleryRepositoryImpl: SupportRepositoryImpl<GalleryImageEntity>(),
        IGalleryRepository {

    /**
     * Find By Id
     */
    override fun findById(id: String): GalleryImageEntity? {
        Timber.d("Find By -> $id")
        val realm = Realm.getDefaultInstance()
        val realmResult = realm.where(GalleryImageEntity::class.java)
                .equalTo("id", id).findFirst()
        var galleryImageEntity: GalleryImageEntity? = null
        if(realmResult != null)
            galleryImageEntity = realm.copyFromRealm(realmResult)
        realm.close()
        return galleryImageEntity
    }

    /**
     * Find By Ids
     */
    override fun findByIds(ids: List<String>): List<GalleryImageEntity> {
        val realm = Realm.getDefaultInstance()
        val galleryImageList = realm.copyFromRealm(
                realm.where(GalleryImageEntity::class.java)
                        .`in`("id", ids.toTypedArray())
                        .findAll())
        realm.close()
        return galleryImageList
    }

    /**
     * Delete
     * @param model
     */
    override fun delete(model: GalleryImageEntity) {
        Timber.d("Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        val galleryImage = realm.where(GalleryImageEntity::class.java)
                .equalTo("displayName", model.displayName)
                .findFirst()
        realm.executeTransaction {
            galleryImage?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Delete
     * @param modelList
     */
    override fun delete(modelList: List<GalleryImageEntity>) {
        for(model in modelList) delete(model)
    }

    /**
     * Delete All
     */
    override fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.delete(GalleryImageEntity::class.java)
        }
        realm.close()
    }

    /**
     * List
     */
    override fun list(): List<GalleryImageEntity> {
        val realm = Realm.getDefaultInstance()
        val galleryImageList =
                realm.copyFromRealm(
                        realm.where(GalleryImageEntity::class.java).findAll())
        realm.close()
        return galleryImageList
    }
}