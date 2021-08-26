package com.example.tecfoodv6.Utils.Conections.ApiTecFood

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConnection {

    fun Retrofit() : Retrofit {

        val httpLoggingInterceptor =  HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.tecfood.club/74054946816/api/")
            .client(okHttpClient)
            .build()

        return retrofit

    }

    fun getService() : ApiService {
        return Retrofit().create(ApiService::class.java)
    }

}