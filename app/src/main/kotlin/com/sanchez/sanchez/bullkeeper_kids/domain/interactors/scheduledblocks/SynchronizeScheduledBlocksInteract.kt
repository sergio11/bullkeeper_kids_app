package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.scheduledblocks

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppAllowedByScheduledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.entity.ScheduledBlockEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IScheduledBlocksService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppAllowedByScheduledRepository
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
            private val appAllowedByScheduledRepository: IAppAllowedByScheduledRepository,
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

        response.data?.map {scheduledBlock ->
            Pair(ScheduledBlockEntity(
                    id = scheduledBlock.identity,
                    name = scheduledBlock.name,
                    enable = scheduledBlock.enable,
                    repeatable = scheduledBlock.repeatable,
                    createAt = scheduledBlock.createAt,
                    image = scheduledBlock.image,
                    kid = scheduledBlock.kid,
                    startAt = scheduledBlock.startAt?.toString(fmt),
                    endAt = scheduledBlock.endAt?.toString(fmt),
                    weeklyFrequency = scheduledBlock.weeklyFrequency?.joinToString(","),
                    allowCalls = scheduledBlock.allowCalls,
                    description = scheduledBlock.description), scheduledBlock
                        .appsAllowed?.filter { it.terminal != null && it.terminal?.identity.equals(preferenceRepository.getPrefTerminalIdentity()) }
                            ?.map { AppAllowedByScheduledEntity(
                                identity = String.format("%s_%s", it.app?.packageName, scheduledBlock.identity),
                                app = it.app?.packageName,
                                scheduledBlock = scheduledBlock.identity
                        ) })
        }?.onEach {
            scheduledBlocksRepository.save(it.first)
            it.second?.let { appAllowedByScheduledRepository.save(it) }
        }
    }
}