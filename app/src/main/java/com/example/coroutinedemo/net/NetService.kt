package com.example.coroutinedemo.net

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Streaming
import retrofit2.http.Url

interface NetService {

    @Headers("Accept-Encoding: identity")
    @Streaming
    @GET
    fun downloadFile(@Url url: String): Call<ResponseBody>

    companion object {
//        private const val baseUrl: String = "http://a.downpp.com/apk5/"
//        private const val baseUrl: String = "https://github.com/kevin-leak/CoroutineDemo/tree/master/art/"
        private const val baseUrl: String = "https://f-droid.org/"

        @Volatile
        private var instance: NetService? = null
        fun getInstance(): NetService = instance ?: synchronized(NetService::class.java) {
            instance ?: Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(NetService::class.java)
                .also { instance = it }
        }
    }

}