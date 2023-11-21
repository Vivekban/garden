package com.garden.data.api

import com.garden.BuildConfig
import com.garden.data.common.DataConstant.BASE_URL
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Client for making networking calls
 */
class ApiClient {

    companion object {

        fun getClient(): Retrofit {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            // This will add API key as query parameter to each api
            val apiKeyInterceptor = Interceptor { chain ->
                val original: Request = chain.request()
                val originalHttpUrl: HttpUrl = original.url
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("key", BuildConfig.API_KEY)
                    .build()
                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(url)
                chain.proceed(requestBuilder.build())
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor)
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}
