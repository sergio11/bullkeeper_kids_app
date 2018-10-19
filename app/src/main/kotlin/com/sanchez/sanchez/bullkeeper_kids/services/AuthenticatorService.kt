package com.sanchez.sanchez.bullkeeper_kids.services

import javax.inject.Inject
import javax.inject.Singleton

/**
 * AuthenticatorService
 */
@Singleton
class AuthenticatorService
@Inject constructor(){
    //Learning purpose: We assume the user is always logged in
    //Here you should put your own logic to return whether the user
    //is authenticated or not
    fun userLoggedIn() = true
}
