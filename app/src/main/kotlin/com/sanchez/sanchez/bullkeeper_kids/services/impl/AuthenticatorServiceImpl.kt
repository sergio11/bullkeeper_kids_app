package com.sanchez.sanchez.bullkeeper_kids.services.impl

import com.sanchez.sanchez.bullkeeper_kids.services.IAuthenticatorService
import javax.inject.Inject

/**
 * Authenticator Service Impl
 */
class AuthenticatorServiceImpl @Inject constructor(): IAuthenticatorService {

    //Learning purpose: We assume the user is always logged in
    //Here you should put your own logic to return whether the user
    //is authenticated or not
    override fun userLoggedIn() = false

}