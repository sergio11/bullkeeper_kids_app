package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.account

import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.functional.Either
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.services.IAuthenticatorService
import javax.inject.Inject


/**
 * Authenticate Interact
 */
@PerActivity
class AuthenticateInteract
    @Inject constructor(private val authenticateService: IAuthenticatorService):
        UseCase<String, AuthenticateInteract.Params>(){


    /**
     * Run Interact
     */
    override suspend fun run(params: Params): Either<Failure, String> {
        Preconditions.checkNotNull(params, "Params can not be null")
        
        return try {
            val authToken = "hola"
            Either.Right(authToken)
        } catch (exception: Throwable) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError())
        }
    }


    /**
     * Interact Params
     */
    data class Params(val email: String, val password: String)


}