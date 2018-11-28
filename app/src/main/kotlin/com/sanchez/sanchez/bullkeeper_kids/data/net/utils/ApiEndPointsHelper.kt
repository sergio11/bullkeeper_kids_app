package com.sanchez.sanchez.bullkeeper_kids.data.net.utils

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

}
