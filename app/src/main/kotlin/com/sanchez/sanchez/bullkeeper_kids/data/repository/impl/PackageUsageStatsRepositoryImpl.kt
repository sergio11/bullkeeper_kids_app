package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl


import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageUsageStatsRepository
import com.sanchez.sanchez.bullkeeper_kids.data.entity.SystemPackageUsageStatsEntity
import io.realm.Realm

/**
 * Package Usage Stats Repository
 */
class PackageUsageStatsRepositoryImpl: SupportRepositoryImpl<SystemPackageUsageStatsEntity>(),
            IPackageUsageStatsRepository {

    /**
     *
     */
    override fun list(): List<SystemPackageUsageStatsEntity> {
        val realm = Realm.getDefaultInstance()
        val systemPackageUsageStatsList =
                realm.copyFromRealm(
                        realm.where(SystemPackageUsageStatsEntity::class.java).findAll())
        realm.close()
        return systemPackageUsageStatsList
    }

    /**
     * Delete
     */
    override fun delete(model: SystemPackageUsageStatsEntity) {
        val realm = Realm.getDefaultInstance()
        // Find Package
        val packageToDelete = realm.where(SystemPackageUsageStatsEntity::class.java)
                .equalTo("packageName", model.packageName)
                .findFirst()
        // Remove package into writable transaction
        realm.executeTransaction {
            packageToDelete?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Delete All
     */
    override fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.delete(SystemPackageUsageStatsEntity::class.java)
        }
        realm.close()
    }


    /**
     * Delete
     */
    override fun delete(modelList: List<SystemPackageUsageStatsEntity>) {
       for(model in modelList) delete(model)
    }


}