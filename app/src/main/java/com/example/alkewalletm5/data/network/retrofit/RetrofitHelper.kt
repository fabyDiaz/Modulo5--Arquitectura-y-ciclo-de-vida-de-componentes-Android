package com.example.alkewalletm5.data.network.retrofit

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    fun getRetrofit(token: String? = null): Retrofit {
        val clientBuilder = OkHttpClient.Builder()

        if (token != null) {
            clientBuilder.addInterceptor(AuthInterceptor(token))
            Log.i("USUARIO",AuthInterceptor(token).toString() )
        }

        val client = clientBuilder.build()

        return Retrofit.Builder()
            .baseUrl("http://wallet-main.eba-ccwdurgr.us-east-1.elasticbeanstalk.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
