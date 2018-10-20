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
 * Save Installed Package Interact
 */
@Singleton
class SaveInstalledPackageInteract
    @Inject constructor(private val systemPackageHelper: ISystemPackageHelper,
                        private val packageInstalledRepository: IPackageInstalledRepository,
                        private val context: Context): UseCase<String, SaveInstalledPackageInteract.Params>() {


    val TAG = "SAVE_PACKAGE"

    /**
     * Run Interact
     */
    override suspend fun run(params: Params): Either<Failure, String> {
        Log.d(TAG,  "Save Package -> ${params.packageName}")

        return try {

            val packageInfo = systemPackageHelper.getPackageInfo(context.packageManager,
                    params.packageName.replace("package:", ""))

            packageInfo?.let {
                Log.d(TAG, "Package Info obtained")
                it.prettyPrint()
                // Save Package
                packageInstalledRepository.save(it)
                Either.Right(it.appName)
            } ?: run {
                Log.d(TAG, "Package not founded")
                Either.Left(Failure.ServerError())
            }


        } catch (exception: Throwable) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError())
        }

    }


    /**
     * Interact Params
     */
    data class Params(val packageName: String)

}