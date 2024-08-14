package com.moraga.libros.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    public fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jadg13.online/daa2/public/api/books/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}