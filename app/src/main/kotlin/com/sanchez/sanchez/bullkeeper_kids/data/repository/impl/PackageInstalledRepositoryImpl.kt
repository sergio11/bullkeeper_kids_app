package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.data.entity.PackageInstalledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageInfo
import io.realm.Realm
import javax.inject.Singleton

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
        Realm.Transaction {
            val packageInstalledEntity = PackageInstalledEntity()
            packageInstalledEntity.appName = model.appName
            packageInstalledEntity.firstInstallTime = model.firstInstallTime
            packageInstalledEntity.lastUpdateTime = model.lastUpdateTime
            packageInstalledEntity.packageName = model.packageName
            packageInstalledEntity.versionCode = model.versionCode
            packageInstalledEntity.versionName = model.versionName
            it.insertOrUpdate(packageInstalledEntity)
            it.commitTransaction()
        }



    }

    /**
     * Save
     */
    override fun save(modelList: List<SystemPackageInfo>) {
        for(model in modelList) save(model)
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
        }
        return systemPackageInfoList
    }

}