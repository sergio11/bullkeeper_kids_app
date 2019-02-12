package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.core.extension.toIntArray
import com.sanchez.sanchez.bullkeeper_kids.core.extension.toLocalTime
import com.sanchez.sanchez.bullkeeper_kids.data.entity.ScheduledBlockEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IScheduledBlocksRepository
import io.realm.Realm
import org.joda.time.LocalTime
import timber.log.Timber
import java.util.*

/**
 * Scheduled Blocks Repository
 */
class ScheduledBlocksRepositoryImpl: SupportRepositoryImpl<ScheduledBlockEntity>(),
    IScheduledBlocksRepository{

    /**
     * Delete
     */
    override fun delete(model: ScheduledBlockEntity) {
        Timber.d("Delete Model -> $model")
        val realm = Realm.getDefaultInstance()
        val scheduledBlock = realm.where(ScheduledBlockEntity::class.java)
                .equalTo("id", model.id)
                .findFirst()
        realm.executeTransaction {
            scheduledBlock?.deleteFromRealm()
        }
        realm.close()
    }

    /**
     * Delete
     */
    override fun delete(modelList: List<ScheduledBlockEntity>) {
        for(model in modelList) delete(model)
    }

    /**
     * List
     */
    override fun list(): List<ScheduledBlockEntity> {
        val realm = Realm.getDefaultInstance()
        val scheduledBlockist =
                realm.copyFromRealm(
                        realm.where(ScheduledBlockEntity::class.java).findAll())
        realm.close()
        return scheduledBlockist
    }

    /**
     * Delete All
     */
    override fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.delete(ScheduledBlockEntity::class.java)
        }
        realm.close()
    }

    /**
     * Delete By Kid
     */
    override fun deleteByKid(kid: String) {
        val realm = Realm.getDefaultInstance()
        val scheduledBlockByKid =
                realm.where(ScheduledBlockEntity::class.java)
                .equalTo("id", kid)
                .findAll()
        realm.executeTransaction {
            scheduledBlockByKid?.deleteAllFromRealm()
        }
        realm.close()
    }

    /**
     * Delete By Kid And Block
     */
    override fun deleteByKidAndBlock(kid: String, block: String) {
        val realm = Realm.getDefaultInstance()
        val scheduledBlockByKid =
                realm.where(ScheduledBlockEntity::class.java)
                        .equalTo("id", kid)
                        .equalTo("id", block)
                        .findAll()
        realm.executeTransaction {
            scheduledBlockByKid?.deleteAllFromRealm()
        }
        realm.close()
    }

    /**
     * Update Status
     */
    override fun updateStatus(id: String, status: Boolean) {
        val realm = Realm.getDefaultInstance()
        realm.where(ScheduledBlockEntity::class.java)
                .equalTo("id", id)
                .findFirst()?.let {block ->
                    realm.executeTransaction { realm ->
                        block.enable = status
                        realm.insertOrUpdate(block)
                    }
                }
        realm.close()
    }

    /**
     * Update Image
     */
    override fun updateImage(id: String, image: String) {
        val realm = Realm.getDefaultInstance()
        realm.where(ScheduledBlockEntity::class.java)
                .equalTo("id", id)
                .findFirst()?.let {block ->
                    realm.executeTransaction { realm ->
                        block.image = image
                        realm.insertOrUpdate(block)
                    }
                }
        realm.close()
    }

    /**
     * Any Scheduled Block Enable
     */
    override fun anyScheduledBlockEnable(now: LocalTime, localTimeFormat: String): Boolean
        = getScheduledBlockEnableFor(LocalTime.now(), localTimeFormat) != null

    /**
     * Any Scheduled Block Enable For This Moment
     */
    override fun anyScheduledBlockEnableForThisMoment(localTimeFormat: String): Boolean
        = anyScheduledBlockEnable(LocalTime.now(), localTimeFormat)

    /**
     * Get Scheduled Block Enable For
     */
    override fun getScheduledBlockEnableFor(moment: LocalTime, localTimeFormat: String): ScheduledBlockEntity? {
        val scheduledBlocks = list()
        var scheduledBlockEnabled: ScheduledBlockEntity? = null
        for(scheduledBlock in scheduledBlocks) {
            if(scheduledBlock.enable) {
                // Start At
                val startAt = scheduledBlock.startAt?.toLocalTime(localTimeFormat)
                // End At
                val endAt = scheduledBlock.endAt?.toLocalTime(localTimeFormat)
                // Weekly Frequency
                val weeklyFrequency = scheduledBlock.weeklyFrequency?.toIntArray()
                val calendar = Calendar.getInstance()
                if(weeklyFrequency?.getOrNull(when(calendar.get(Calendar.DAY_OF_WEEK)) {
                            Calendar.MONDAY -> 0
                            Calendar.TUESDAY -> 1
                            Calendar.WEDNESDAY -> 2
                            Calendar.THURSDAY -> 3
                            Calendar.FRIDAY -> 4
                            Calendar.SATURDAY -> 5
                            Calendar.SUNDAY -> 6
                            else -> -1
                        }) == 1) {
                    if(moment.isAfter(startAt) && moment.isBefore(endAt)) {
                        scheduledBlockEnabled = scheduledBlock
                        break
                    }
                }
            }
        }
        return scheduledBlockEnabled
    }

    /**
     * Get Scheduled Block Enable For This Moment
     */
    override fun getScheduledBlockEnableForThisMoment(localTimeFormat: String): ScheduledBlockEntity?
        = getScheduledBlockEnableFor(LocalTime.now(), localTimeFormat)

}