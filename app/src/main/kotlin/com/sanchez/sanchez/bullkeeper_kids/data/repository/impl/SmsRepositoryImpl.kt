package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.entity.SmsEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.ISmsRepository

/**
 * Sms Repository
 */
class SmsRepositoryImpl: SupportRepositoryImpl<SmsEntity>(), ISmsRepository {


    /**
     * Delete
     */
    override fun delete(model: SmsEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    /**
     * Delete
     */
    override fun delete(modelList: List<SmsEntity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * List
     */
    override fun list(): List<SmsEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}