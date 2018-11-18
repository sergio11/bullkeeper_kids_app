package com.sanchez.sanchez.bullkeeper_kids.data.net.utils

class ApiEndPointsHelper
/**
 * Api End Points Helper
 * @param baseUrl
 */
(private val baseUrl: String) {

    /**
     * Get Son Profile Url
     * @param identity
     * @return
     */
    fun getSonProfileUrl(identity: String): String {
        return baseUrl + String.format("images/children/%s", identity)
    }


    /**
     * Get Parent Profile Url
     * @param identity
     * @return
     */
    fun getParentProfileUrl(identity: String): String {
        return baseUrl + String.format("images/parents/%s", identity)
    }

}
