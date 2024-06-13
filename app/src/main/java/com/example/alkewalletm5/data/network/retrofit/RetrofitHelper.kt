package com.example.alkewalletm5.data.network.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

   fun getRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl("http://wallet-main.eba-ccwdurgr.us-east-1.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
