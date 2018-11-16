package com.sanchez.sanchez.bullkeeper_kids.services

/**
 * Authenticator Service
 */
interface IAuthenticatorService {

    /**
     * User Logged In
     */
    fun userLoggedIn(): Boolean
}