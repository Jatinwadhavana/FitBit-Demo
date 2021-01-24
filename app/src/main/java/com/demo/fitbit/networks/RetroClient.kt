package com.demo.fitbit.networks

import com.demo.fitbit.commons.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Retrofit Singleton class
 */
object RetroClient {
    private var retrofit: Retrofit? = null
    val retrofitClient: Retrofit?
        get() {
            if (retrofit == null) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)

                val client = OkHttpClient.Builder().addInterceptor(logging)

                retrofit = Retrofit.Builder()
                        .client(client.build())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(Constants.BASE_URL)
                        .build()
            }
            return retrofit
        }
}