package com.example.coroutinedemo.net

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface NetService {


    @Streaming
    @GET
    fun downloadFile(@Url url: String): Call<ResponseBody>

    companion object {
//        private const val baseUrl: String = "http://a.downpp.com/apk5/"
        private const val baseUrl: String = "http://acj6.0098118.com/pc6_soure/"
//        private const val baseUrl: String = "http://https://f-droid.org/"

        @Volatile
        private var instance: NetService? = null
        fun getInstance(): NetService = instance ?: synchronized(NetService::class.java) {
            instance ?: Retrofit.Builder()
                .baseUrl(baseUrl)
                .build()
                .create(NetService::class.java)
                .also { instance = it }
        }
    }

}