package com.sanchez.sanchez.bullkeeper_kids.domain.utils

/**
 * Auth Token Aware
 */
interface IAuthTokenAware {

    /**
     * Get Auth Token
     * @return
     */
    fun getAuthToken(): String

    /**
     * Set Auth Token
     * @param token
     */
     fun setAuthToken(token: String)
}