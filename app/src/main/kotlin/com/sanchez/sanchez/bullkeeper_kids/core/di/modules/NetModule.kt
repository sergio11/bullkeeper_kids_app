package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton
import com.sanchez.sanchez.bullkeeper_kids.domain.utils.IAuthTokenAware
import com.fasterxml.jackson.databind.module.SimpleModule
import com.jakewharton.picasso.OkHttp3Downloader
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sanchez.sanchez.bullkeeper_kids.data.net.interceptors.AuthTokenInterceptor
import com.squareup.picasso.Picasso
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.fasterxml.jackson.databind.ObjectMapper
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.data.net.deserializers.BirthdayDeserializer
import com.sanchez.sanchez.bullkeeper_kids.data.net.deserializers.JodaLocalTimeDeserializer
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import org.joda.time.LocalTime
import java.util.*
import retrofit2.converter.jackson.JacksonConverterFactory;
import com.here.oksse.OkSse
import java.util.concurrent.TimeUnit


/**
 * Net Module
 */
@Module
class NetModule(private val application: AndroidApplication) {


    /**
     * Provide Auth Token Inteceptor
     * @param authTokenAware
     * @return
     */
    @Singleton
    @Provides
    fun provideAuthTokenInterceptor(authTokenAware: IAuthTokenAware): AuthTokenInterceptor {
        return AuthTokenInterceptor(authTokenAware)
    }

    /**
     * Provide Retrofit
     */
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, mapper: ObjectMapper): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client)
                .build()
    }

    /**
     * Provide Object Mapper
     * @return
     */
    @Singleton
    @Provides
    fun provideObjectMapper(appContext: Context): ObjectMapper {
        val mapper = ObjectMapper()
        val module = SimpleModule()
        module.addDeserializer(LocalTime::class.java,
                JodaLocalTimeDeserializer(
                        appContext.getString(R.string.joda_local_time_format_server_response)))
        mapper.registerModule(module)
        return mapper
    }


    /**
     * Provide Picasso
     * @param client
     * @param context
     * @return
     */
    @Singleton
    @Provides
    fun providePicasso(client: OkHttpClient, context: Context): Picasso {
        return Picasso.Builder(context)
                .downloader(OkHttp3Downloader(client))
                .build()
    }

    /**
     * Provide HTTP Client
     * @return
     */
    @Singleton
    @Provides
    fun provideHttpClient(authTokenInterceptor: AuthTokenInterceptor): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                    .addInterceptor(authTokenInterceptor)
                    .addNetworkInterceptor(StethoInterceptor())
                    .readTimeout(0, TimeUnit.MILLISECONDS)
                    .build()
        } else {
            OkHttpClient.Builder()
                    .addInterceptor(authTokenInterceptor)
                    .readTimeout(0, TimeUnit.MILLISECONDS)
                    .build()
        }
    }


    /**
     * Provide Api End Points Helper
     */
    @Provides
    @Singleton
    internal fun provideApiEndPointsHelper(): ApiEndPointsHelper {
        return ApiEndPointsHelper(BuildConfig.BASE_URL)
    }

    /**
     * Provide Ok Sse
     * @param okHttpClient
     * @return
     */
    @Singleton
    @Provides
    internal fun provideOkSse(okHttpClient: OkHttpClient): OkSse {
        return OkSse(okHttpClient)
    }
}