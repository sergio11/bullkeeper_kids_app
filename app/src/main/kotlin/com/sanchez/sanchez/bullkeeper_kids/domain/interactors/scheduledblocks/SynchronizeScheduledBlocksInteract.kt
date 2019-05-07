package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.scheduledblocks


import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppAllowedByScheduledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.entity.ScheduledBlockEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IScheduledBlocksService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppAllowedByScheduledRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IScheduledBlocksRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Synchronize Scheduled Blocks
 */
class SynchronizeScheduledBlocksInteract
    @Inject constructor(
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

        val scheduledBlocksResponseList = response.data?.map {scheduledBlock ->
            Pair(ScheduledBlockEntity(
                    id = scheduledBlock.identity,
                    name = scheduledBlock.name,
                    enable = scheduledBlock.enable,
                    repeatable = scheduledBlock.repeatable,
                    createAt = scheduledBlock.createAt,
                    image = scheduledBlock.image,
                    kid = scheduledBlock.kid,
                    startAt = scheduledBlock.startAt?.toString(),
                    endAt = scheduledBlock.endAt?.toString(),
                    weeklyFrequency = scheduledBlock.weeklyFrequency?.joinToString(","),
                    allowCalls = scheduledBlock.allowCalls,
                    description = scheduledBlock.description), scheduledBlock
                        .appsAllowed?.filter { it.terminal != null && it.terminal?.identity.equals(preferenceRepository.getPrefTerminalIdentity()) }
                            ?.map { AppAllowedByScheduledEntity(
                                identity = String.format("%s_%s", it.app?.packageName, scheduledBlock.identity),
                                app = it.app?.packageName,
                                scheduledBlock = scheduledBlock.identity
                        ) })
        }?.toList()

        if(!scheduledBlocksResponseList.isNullOrEmpty()) {

            scheduledBlocksResponseList.forEach {scheduledBlockResponse ->
                scheduledBlocksRepository.save(scheduledBlockResponse.first)
                scheduledBlockResponse.second?.let {
                    appAllowedByScheduledRepository.deleteAll()
                    appAllowedByScheduledRepository.save(it) }
            }

        }  else {

            scheduledBlocksRepository.deleteAll()
            appAllowedByScheduledRepository.deleteAll()
        }

    }
}