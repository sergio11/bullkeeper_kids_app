package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.data.entity.PackageInstalledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageInfo
import io.realm.Realm
import javax.inject.Singleton
import io.realm.RealmList

/**
 * Package Installed Repository Impl
 */
@Singleton
class PackageInstalledRepositoryImpl: IPackageInstalledRepository {

    val TAG = "PACKAGE_INSTALLED"


    /**
     * Save
     */
    override fun save(model: SystemPackageInfo) {
        Log.d(TAG, "Save Model -> $model")
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val packageInstalledEntity = PackageInstalledEntity()
            packageInstalledEntity.appName = model.appName
            packageInstalledEntity.firstInstallTime = model.firstInstallTime
            packageInstalledEntity.lastUpdateTime = model.lastUpdateTime
            packageInstalledEntity.packageName = model.packageName
            packageInstalledEntity.versionCode = model.versionCode
            packageInstalledEntity.versionName = model.versionName
            packageInstalledEntity.isBlocked = model.isBlocked
            it.insertOrUpdate(packageInstalledEntity)
        }
        realm.close()
    }

    /**
     * Save
     */
    override fun save(modelList: List<SystemPackageInfo>) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val listToSave = RealmList<PackageInstalledEntity>()
            for (model in modelList) {
                val packageInstalledEntity = PackageInstalledEntity()
                packageInstalledEntity.appName = model.appName
                packageInstalledEntity.firstInstallTime = model.firstInstallTime
                packageInstalledEntity.lastUpdateTime = model.lastUpdateTime
                packageInstalledEntity.packageName = model.packageName
                packageInstalledEntity.versionCode = model.versionCode
                packageInstalledEntity.versionName = model.versionName
                packageInstalledEntity.isBlocked = model.isBlocked
                listToSave.add(packageInstalledEntity)
            }
            it.insertOrUpdate(listToSave)
        }
        realm.close()
    }

    /**
     * List
     */
    override fun list(): List<SystemPackageInfo> {
        val systemPackageInfoList = ArrayList<SystemPackageInfo>()
        val realm = Realm.getDefaultInstance()
        val realmResults = realm.where(PackageInstalledEntity::class.java).findAll()
        for (realmResult in realmResults) {
            val systemPackageInfo = SystemPackageInfo()
            systemPackageInfo.appName = realmResult.appName
            systemPackageInfo.packageName = realmResult.packageName
            systemPackageInfo.firstInstallTime = realmResult.firstInstallTime
            systemPackageInfo.lastUpdateTime = realmResult.lastUpdateTime
            systemPackageInfo.versionCode = realmResult.versionCode
            systemPackageInfo.versionName = realmResult.versionName
            systemPackageInfo.isBlocked = realmResult.isBlocked
            systemPackageInfoList.add(systemPackageInfo)
        }
        realm.close()
        return systemPackageInfoList
    }

    /**
     * Delete
     */
    override fun delete(model: SystemPackageInfo) {
        Log.d(TAG, "Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        // Find Package
        val packageToDelete = realm.where(PackageInstalledEntity::class.java)
                .equalTo("packageName", model.packageName)
                .findFirst()
        // Remove package into writable transaction
        realm.executeTransaction {
            packageToDelete?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Delete
     */
    override fun delete(modelList: List<SystemPackageInfo>) {
        for(model in modelList) delete(model)
    }

    /**
     * Find By Package Name
     */
    override fun findByPackageName(packageName: String): SystemPackageInfo? {
        Log.d(TAG, "Find package $packageName")
        val realm = Realm.getDefaultInstance()
        val packageInstalled = realm.where(PackageInstalledEntity::class.java)
                .equalTo("packageName", packageName).findFirst()
        return packageInstalled?.let {
            val systemPackageInfo = SystemPackageInfo()
            systemPackageInfo.appName = it.appName
            systemPackageInfo.packageName = it.packageName
            systemPackageInfo.firstInstallTime = it.firstInstallTime
            systemPackageInfo.lastUpdateTime = it.lastUpdateTime
            systemPackageInfo.versionCode = it.versionCode
            systemPackageInfo.versionName = it.versionName
            systemPackageInfo.isBlocked = it.isBlocked
            realm.close()
            return systemPackageInfo
        }

    }


    /**
     * Get Blocked Packages
     */
    override fun getBlockedPackages(): List<SystemPackageInfo> {
        val systemPackageInfoList = ArrayList<SystemPackageInfo>()
        val realm = Realm.getDefaultInstance()
        val packagesBlocked = realm.where(PackageInstalledEntity::class.java)
                .equalTo("isBlocked", true).findAll()
        for (packageBlocked in packagesBlocked) {
            val systemPackageInfo = SystemPackageInfo()
            systemPackageInfo.appName = packageBlocked.appName
            systemPackageInfo.packageName = packageBlocked.packageName
            systemPackageInfo.firstInstallTime = packageBlocked.firstInstallTime
            systemPackageInfo.lastUpdateTime = packageBlocked.lastUpdateTime
            systemPackageInfo.versionCode = packageBlocked.versionCode
            systemPackageInfo.versionName = packageBlocked.versionName
            systemPackageInfo.isBlocked = packageBlocked.isBlocked
            systemPackageInfoList.add(systemPackageInfo)
        }
        realm.close()
        return systemPackageInfoList
    }

}