package com.glass.cienciageek.data.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClientInstance {

    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://fcm.googleapis.com/fcm/"

    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}