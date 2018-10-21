package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.functional.Either
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageUsageStatsRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import javax.inject.Inject

/**
 * Syn Package Usage Stats Interact
 */
class SynPackageUsageStatsInteract
    @Inject constructor(private val usageStatsService: IUsageStatsService,
                        private val packageUsageStatsRepository: IPackageUsageStatsRepository)
    : UseCase<Int, UseCase.None>() {

    val TAG = "SYC_PACKAGE_USAGE"

    /**
     * Run Interact
     */
    override suspend fun run(params: None): Either<Failure, Int> {
        return try {
            // Get Daily Stats
            val usageStatsList =  usageStatsService.getDailyStatsFromAYear()
            Log.d(TAG, "Sync ${usageStatsList.size} usage")
            if(usageStatsList.isNotEmpty()) {
                packageUsageStatsRepository.save(usageStatsList)
            }
            Either.Right(usageStatsList.size)
        } catch (exception: Throwable) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError())
        }
    }
}