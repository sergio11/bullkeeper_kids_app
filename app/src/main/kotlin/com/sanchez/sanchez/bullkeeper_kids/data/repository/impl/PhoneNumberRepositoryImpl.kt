package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPhoneNumberRepository
import com.sanchez.sanchez.bullkeeper_kids.data.entity.PhoneNumberBlockedEntity

/**
 * Phone Number Repository Impl
 */
class PhoneNumberRepositoryImpl: SupportRepositoryImpl<PhoneNumberBlockedEntity>(), IPhoneNumberRepository {

    /**
     * Delete
     */
    override fun delete(model: PhoneNumberBlockedEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Delete
     */
    override fun delete(modelList: List<PhoneNumberBlockedEntity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * List
     */
    override fun list(): List<PhoneNumberBlockedEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}