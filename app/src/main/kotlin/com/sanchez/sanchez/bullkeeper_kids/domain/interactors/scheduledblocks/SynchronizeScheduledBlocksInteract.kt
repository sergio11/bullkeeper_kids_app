package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.scheduledblocks

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.ScheduledBlockEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IScheduledBlocksService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IScheduledBlocksRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject
import org.joda.time.format.DateTimeFormat

/**
 * Synchronize Scheduled Blocks
 */
class SynchronizeScheduledBlocksInteract
    @Inject constructor(
            private val appContext: Context,
            retrofit: Retrofit,
            private val scheduledBlocksService: IScheduledBlocksService,
            private val scheduledBlocksRepository: IScheduledBlocksRepository,
            private val preferenceRepository: IPreferenceRepository
    ): UseCase<Unit, UseCase.None>(retrofit){


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None) {

        val kid = preferenceRepository.getPrefKidIdentity()

        val response = scheduledBlocksService.getAllScheduledBlocks(kid)
                .await()

        val fmt = DateTimeFormat.forPattern(
                appContext.getString(R.string.joda_local_time_format_server_response))

        response.data?.map {
            ScheduledBlockEntity(
                    id = it.identity,
                    name = it.name,
                    enable = it.enable,
                    repeatable = it.repeatable,
                    createAt = it.createAt,
                    image = it.image,
                    kid = it.kid,
                    startAt = it.startAt?.toString(fmt),
                    endAt = it.endAt?.toString(fmt),
                    weeklyFrequency = it.weeklyFrequency?.joinToString(","),
                    allowCalls = it.allowCalls,
                    description = it.description)
        }?.let { scheduledBlocksToSave ->
            scheduledBlocksRepository.save(scheduledBlocksToSave)
        }
    }
}