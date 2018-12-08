package com.sanchez.sanchez.bullkeeper_kids.data.net.utils

import com.fernandocejas.arrow.checks.Preconditions

class ApiEndPointsHelper
/**
 * Api End Points Helper
 * @param baseUrl
 */
(private val baseUrl: String) {

    /**
     * Get Kid Profile Url
     * @param identity
     * @return
     */
    fun getKidProfileUrl(identity: String): String {
        return baseUrl + String.format("images/children/%s", identity)
    }


    /**
     * Get Guardians Profile Url
     * @param identity
     * @return
     */
    fun getGuardiansProfileUrl(identity: String): String {
        return baseUrl + String.format("images/guardians/%s", identity)
    }

    /**
     * Get Event Subscription URL
     * @param subscriberId
     * @return
     */
    fun getEventSubscriptionUrl(subscriberId: String): String {
        Preconditions.checkNotNull(subscriberId, "Subscriber id can not be null")
        Preconditions.checkState(!subscriberId.isEmpty(), "Subscriber id can not be empty")
        return baseUrl + String.format("events/subscribe/%s", subscriberId)
    }

}
