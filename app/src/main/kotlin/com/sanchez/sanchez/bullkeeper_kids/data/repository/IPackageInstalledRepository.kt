package com.sanchez.sanchez.bullkeeper_kids.data.repository

import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageInfo

/**
 * IPackage Installed Repository
 */
interface IPackageInstalledRepository: ISupportRepository<SystemPackageInfo> {

    /**
     * Find By Package Name
     */
    fun findByPackageName(packageName: String): SystemPackageInfo?

    /**
     * Get Blocked Packages
     */
    fun getBlockedPackages(): List<SystemPackageInfo>

}