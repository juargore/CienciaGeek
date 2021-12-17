package com.glass.cienciageek.data

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FirebaseApi {

    @Headers(
        "Content-Type: application/json",
        "Authorization: key=AAAArM-Eyeo:APA91bE1-UdOg5c5gOS9dyteO00nGbzcyEBFka5fOZiKFCSJMuyYNjoyu1zimjsU5LzdZGBXAZUf9f6XVdWcy4ODIYSKbvOmn4v1o9ep6BtVNfLlLSxu_bqmlhKN5DIOhB5ndK9ivEnJ"
    )
    @POST("send")
    fun sendPushNotification(@Body jsonObject: RequestBody): Call<Unit>

}