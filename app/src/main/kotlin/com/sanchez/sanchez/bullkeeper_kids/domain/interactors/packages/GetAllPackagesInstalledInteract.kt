package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.functional.Either
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageInfo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Get All Packages Installed Interact
 */
@Singleton
class GetAllPackagesInstalledInteract
    @Inject constructor(private val packageInstalledRepository: IPackageInstalledRepository): UseCase<List<SystemPackageInfo>, UseCase.None>() {


    /**
     * Run Interact
     */
    override suspend fun run(params: None): Either<Failure, List<SystemPackageInfo>> {

        return try {

            val packageInstalled: List<SystemPackageInfo>  =
                    packageInstalledRepository.list()

            Either.Right(packageInstalled)

        } catch (exception: Throwable) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError())
        }

    }
}