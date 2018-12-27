package com.sanchez.sanchez.bullkeeper_kids.data.repository

import com.sanchez.sanchez.bullkeeper_kids.data.entity.PhoneNumberBlockedEntity

/**
 * Phone Number Repository
 */
interface IPhoneNumberRepository: ISupportRepository<PhoneNumberBlockedEntity> {

    /**
     * Find By Phone Number
     */
    fun findByPhoneNumber(phoneNumber: String): PhoneNumberBlockedEntity?

}