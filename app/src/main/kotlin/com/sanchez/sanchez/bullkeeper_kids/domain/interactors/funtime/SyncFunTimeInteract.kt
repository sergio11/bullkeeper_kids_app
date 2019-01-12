package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.funtime

import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.FunTimeDayScheduledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IFunTimeService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IFunTimeDayScheduledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject

/**
 * Sync Fun Time Interact
 */
class SyncFunTimeInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val funTimeService: IFunTimeService,
            private val funTimeDayScheduledRepository: IFunTimeDayScheduledRepository,
            private val preferencesRepository: IPreferenceRepository
    ): UseCase<Unit, UseCase.None>(retrofit) {


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None) {

        // Get Kid Identity
        val kid = preferencesRepository.getPrefKidIdentity()

        // Get Fun Time Scheduled
        val response = funTimeService
                .getFunTimeScheduled(kid).await()

        response.httpStatus?.let {
            if (it == "OK") {
                response.data?.let {funTimeScheduledDTO ->

                    // Set Fun Time Enable
                    preferencesRepository.setFunTimeEnabled(funTimeScheduledDTO.enabled)
                    // Save Fun Time Day Scheduled
                    funTimeDayScheduledRepository.save(Arrays.asList(
                            // Add Monday
                            FunTimeDayScheduledEntity(
                                    day = funTimeScheduledDTO.monday.day,
                                    enabled = funTimeScheduledDTO.monday.enabled,
                                    totalHours = funTimeScheduledDTO.monday.totalHours,
                                    paused = funTimeScheduledDTO.monday.paused,
                                    pausedAt = funTimeScheduledDTO.monday.pausedAt
                            ),
                            // Add Thursday
                            FunTimeDayScheduledEntity(
                                    day = funTimeScheduledDTO.thursday.day,
                                    enabled = funTimeScheduledDTO.thursday.enabled,
                                    totalHours = funTimeScheduledDTO.thursday.totalHours,
                                    paused = funTimeScheduledDTO.thursday.paused,
                                    pausedAt = funTimeScheduledDTO.thursday.pausedAt
                            ),
                            // Add Wednesday
                            FunTimeDayScheduledEntity(
                                    day = funTimeScheduledDTO.wednesday.day,
                                    enabled = funTimeScheduledDTO.wednesday.enabled,
                                    totalHours = funTimeScheduledDTO.wednesday.totalHours,
                                    paused = funTimeScheduledDTO.wednesday.paused,
                                    pausedAt = funTimeScheduledDTO.wednesday.pausedAt
                            ),
                            // Add Tuesday
                            FunTimeDayScheduledEntity(
                                    day = funTimeScheduledDTO.tuesday.day,
                                    enabled = funTimeScheduledDTO.tuesday.enabled,
                                    totalHours = funTimeScheduledDTO.tuesday.totalHours,
                                    paused = funTimeScheduledDTO.tuesday.paused,
                                    pausedAt = funTimeScheduledDTO.tuesday.pausedAt
                            ),
                            // Add Friday
                            FunTimeDayScheduledEntity(
                                    day = funTimeScheduledDTO.friday.day,
                                    enabled = funTimeScheduledDTO.friday.enabled,
                                    totalHours = funTimeScheduledDTO.friday.totalHours,
                                    paused = funTimeScheduledDTO.friday.paused,
                                    pausedAt = funTimeScheduledDTO.friday.pausedAt
                            ),
                            // Add Saturday
                            FunTimeDayScheduledEntity(
                                    day = funTimeScheduledDTO.saturday.day,
                                    enabled = funTimeScheduledDTO.saturday.enabled,
                                    totalHours = funTimeScheduledDTO.saturday.totalHours,
                                    paused = funTimeScheduledDTO.saturday.paused,
                                    pausedAt = funTimeScheduledDTO.saturday.pausedAt
                            ),
                            // Add Sunday
                            FunTimeDayScheduledEntity(
                                    day = funTimeScheduledDTO.sunday.day,
                                    enabled = funTimeScheduledDTO.sunday.enabled,
                                    totalHours = funTimeScheduledDTO.sunday.totalHours,
                                    paused = funTimeScheduledDTO.sunday.paused,
                                    pausedAt = funTimeScheduledDTO.sunday.pausedAt
                            )
                    ))

                }
            }
        }


    }


}