package com.sanchez.sanchez.bullkeeper_kids.core.exception

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {

    /**
     * Network Connection
     */
    object NetworkConnection : Failure()

    /**
     * Server Error
     */
    object ServerError : Failure()

    /**
     * Api Error
     */
    class ApiError(private val response: APIResponse<*>?): Failure()

    /**
     * Unauthorized Request Error
     */
    class UnauthorizedRequestError(): Failure()

    /**
     * Bad Request Error
     */
    class BadRequestError(): Failure()


    /**
     * Unexpected Error
     */
    object UnexpectedError : Failure()


    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure: Failure()
}
