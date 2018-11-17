package com.sanchez.sanchez.bullkeeper_kids.data.net.interceptors

import com.sanchez.sanchez.bullkeeper_kids.domain.utils.IAuthTokenAware
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Auth Token Interceptor
 */
class AuthTokenInterceptor
        constructor(private val authTokenAware: IAuthTokenAware): Interceptor {

    companion object {
        /**
         * Token Header Name
         */
        const val TOKEN_HEADER_NAME = "Authorization"
    }


    /**
     * Intercept
     * @param chain
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (authTokenAware.getAuthToken() != null) {
            request = request.newBuilder()
                    .header(TOKEN_HEADER_NAME, authTokenAware.getAuthToken())
                    .method(request.method(), request.body())
                    .build()
        }
        return chain.proceed(request)
    }
}