package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.entity.ContactEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IContactRepository

/**
 * Contact Repository
 */
class ContactRepositoryImpl: SupportRepositoryImpl<ContactEntity>(), IContactRepository {

    /**
     * Delete
     */
    override fun delete(model: ContactEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Delete
     */
    override fun delete(modelList: List<ContactEntity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * List
     */
    override fun list(): List<ContactEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}