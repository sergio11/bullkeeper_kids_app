package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.data.entity.PackageUsageStatsEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageUsageStatsRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageUsageStats
import io.realm.Realm
import io.realm.RealmList
import javax.inject.Singleton

/**
 * Package Usage Stats Repository
 */
@Singleton
class PackageUsageStatsRepositoryImpl: IPackageUsageStatsRepository {

    val TAG = "PACKAGE_USAGE"

    /**
     * Save
     */
    override fun save(model: SystemPackageUsageStats) {
        Log.d(TAG, "Save Model -> $model")
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val packageUsageStatsEntity = PackageUsageStatsEntity(model.packageName)
            packageUsageStatsEntity.firstTimeStamp = model.firstTimeStamp
            packageUsageStatsEntity.lastTimeStamp = model.lastTimeStamp
            packageUsageStatsEntity.lastTimeUsed = model.lastTimeUsed
            packageUsageStatsEntity.totalTimeInForeground = model.totalTimeInForeground
            realm.insertOrUpdate(packageUsageStatsEntity)
        }
        realm.close()
    }

    /**
     * Delete
     */
    override fun delete(model: SystemPackageUsageStats) {
        Log.d(TAG, "Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        // Find Package
        val packageToDelete = realm.where(PackageUsageStatsEntity::class.java)
                .equalTo("packageName", model.packageName)
                .findFirst()
        // Remove package into writable transaction
        realm.executeTransaction {
            packageToDelete?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Save
     */
    override fun save(modelList: List<SystemPackageUsageStats>) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val listToSave = RealmList<PackageUsageStatsEntity>()
            for (model in modelList) {
                val packageUsageStatsEntity = PackageUsageStatsEntity(model.packageName)
                packageUsageStatsEntity.lastTimeUsed = model.lastTimeUsed
                packageUsageStatsEntity.lastTimeStamp = model.lastTimeStamp
                packageUsageStatsEntity.firstTimeStamp = model.firstTimeStamp
                packageUsageStatsEntity.totalTimeInForeground = model.totalTimeInForeground
                listToSave.add(packageUsageStatsEntity)
            }
            it.insertOrUpdate(listToSave)
        }
        realm.close()
    }

    /**
     * Delete
     */
    override fun delete(modelList: List<SystemPackageUsageStats>) {
       for(model in modelList) delete(model)
    }


    /**
     * List
     */
    override fun list(): List<SystemPackageUsageStats> {
        val systemPackageUsageStatsList = ArrayList<SystemPackageUsageStats>()
        val realm = Realm.getDefaultInstance()
        val realmResults = realm.where(PackageUsageStatsEntity::class.java).findAll()
        for (realmResult in realmResults) {
            val systemPackageUsageStats = SystemPackageUsageStats()
            systemPackageUsageStats.firstTimeStamp = realmResult.firstTimeStamp
            systemPackageUsageStats.lastTimeUsed = realmResult.lastTimeUsed
            systemPackageUsageStats.lastTimeStamp = realmResult.lastTimeStamp
            systemPackageUsageStats.totalTimeInForeground = realmResult.totalTimeInForeground
            systemPackageUsageStats.packageName = realmResult.packageName
            systemPackageUsageStatsList.add(systemPackageUsageStats)
        }
        realm.close()
        return systemPackageUsageStatsList
    }
}