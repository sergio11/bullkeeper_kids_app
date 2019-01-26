package com.sanchez.sanchez.bullkeeper_kids.core.interactor

import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.functional.Either
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.RetrofitException
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
abstract class UseCase<out Type, in Params> constructor(private val retrofit: Retrofit) where Type : Any {

    /**
     * Run
     */
    suspend fun run(params: Params): Either<Failure, Type> {
        Preconditions.checkNotNull(params, "Params can not be null")
        return try {
            val result = onExecuted(params)
            Either.Right(result)
        } catch (exception: Throwable) {
            val retrofitException = asRetrofitException(exception)
            try {
                if (retrofitException.kind === RetrofitException.Kind.NETWORK) {
                    Either.Left(Failure.NetworkConnection)
                } else {
                    val response = retrofitException.getErrorBodyAs(APIResponse::class.java)
                    Either.Left(onApiExceptionOcurred(retrofitException, response))
                }
            } catch (e1: IOException) {
                e1.printStackTrace()
                Either.Left(Failure.UnexpectedError)
            }
        }
    }

    /**
     * On Executed
     */
    abstract suspend fun onExecuted(params: Params): Type

    /**
     * On Api Exception Ocurred
     */
    open fun onApiExceptionOcurred(apiException: RetrofitException, response: APIResponse<*>?): Failure
        =  Failure.ApiError(response)

    /**
     * As Retrofit Exception
     * @param throwable
     * @return
     */
    private fun asRetrofitException(throwable: Throwable): RetrofitException {
        // We had non-200 http error
        if (throwable is HttpException) {
            val response = throwable.response()
            return RetrofitException.httpError(response.raw().request().url().toString(),
                    response, retrofit)
        }
        // A network error happened
        return if (throwable is IOException) {
            RetrofitException.networkError(throwable)
        } else RetrofitException.unexpectedError(throwable)

        // We don't know what happened. We need to simply convert to an unknown error
    }

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
        val job = GlobalScope.async{ run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult(job.await()) }
    }

    class None
}
