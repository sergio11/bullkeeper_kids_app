package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages

import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPackageUsageStatsRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Syn Package Usage Stats Interact
 */
class SynPackageUsageStatsInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val usageStatsService: IUsageStatsService,
            private val packageUsageStatsRepository: IPackageUsageStatsRepository)
    : UseCase<Int, UseCase.None>(retrofit) {


    val TAG = "SYC_PACKAGE_USAGE"

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None): Int {
        // Get Daily Stats
        val usageStatsList =  usageStatsService.getDailyStatsFromAYear()
        Log.d(TAG, "Sync ${usageStatsList.size} usage")
        if(usageStatsList.isNotEmpty()) {
            packageUsageStatsRepository.save(usageStatsList)
        }
        return usageStatsList.size
    }

}