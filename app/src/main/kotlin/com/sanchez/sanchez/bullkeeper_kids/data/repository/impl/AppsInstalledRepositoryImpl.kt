package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl


import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppInstalledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import io.realm.Realm
import timber.log.Timber

/**
 * Package Installed Repository Impl
 */

class AppsInstalledRepositoryImpl: SupportRepositoryImpl<AppInstalledEntity>(), IAppsInstalledRepository {

    /**
     * List
     */
    override fun list(): List<AppInstalledEntity> {
        val realm = Realm.getDefaultInstance()
        val appInstalledList =
                realm.copyFromRealm(
                        realm.where(AppInstalledEntity::class.java).findAll())
        realm.close()
        return appInstalledList
    }

    /**
     * Delete
     */
    override fun delete(model: AppInstalledEntity) {
        Timber.d("Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        // Find Package
        val packageToDelete = realm.where(AppInstalledEntity::class.java)
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
    override fun delete(modelList: List<AppInstalledEntity>) {
        for(model in modelList) delete(model)
    }

    /**
     * Find By Package Name
     */
    override fun findByPackageName(packageName: String): AppInstalledEntity? {
        Timber.d("Find package $packageName")
        val realm = Realm.getDefaultInstance()
        val realmResult = realm.where(AppInstalledEntity::class.java)
                .equalTo("packageName", packageName).findFirst()
        var packageInstalled: AppInstalledEntity? = null
        if(realmResult != null)
            packageInstalled = realm.copyFromRealm(realmResult)
        realm.close()
        return packageInstalled

    }


    /**
     * Get Blocked Packages
     */
    /*override fun getBlockedPackages(): List<AppInstalledEntity> {
        val realm = Realm.getDefaultInstance()
        val packagesBlocked = realm.where(AppInstalledEntity::class.java)
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
    }*/

}