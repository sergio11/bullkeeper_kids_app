package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import android.content.Context
import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.functional.Either
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Synchronize Installed Packages Interact
 */
@Singleton
class SynchronizeInstalledPackagesInteract
    @Inject constructor(private val systemPackageHelper: ISystemPackageHelper,
                        private val packageInstalledRepository: IPackageInstalledRepository): UseCase<Unit, UseCase.None>() {

    val TAG = "SYNC_PACKAGES"

    /**
     * Run Use Case
     */
    override suspend fun run(params: None): Either<Failure, Unit> {
        return try {
            val packageList = systemPackageHelper.getPackages()
            Log.d(TAG, "Packages to sync -> ${packageList.size}")
            val packageCountSaved =  packageInstalledRepository.save(packageList)
            Either.Right(packageCountSaved)
        } catch (exception: Throwable) {
            Either.Left(Failure.ServerError())
        }
    }
}