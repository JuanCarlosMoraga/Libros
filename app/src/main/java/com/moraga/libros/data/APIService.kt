package com.moraga.libros.data

import com.moraga.libros.data.models.BooksResponse
import com.moraga.libros.data.models.Data
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("{endpoint}")
    suspend fun listBooks(
        @Path("endpoint") endpoint: String,
        @Query("name") searchQuery: String? = null
    ): Response<BooksResponse>

    @POST("{endpoint}")
    suspend fun saveBook(@Path("endpoint") endpoint: String, @Body book: Data): Response<Data>

    @PUT("{endpoint}/{id}")
    suspend fun updateBook(@Path("endpoint") endpoint: String, @Path("id") id: Int, @Body book: Data ): Response<Data>

    @DELETE("{endpoint}/{id}")
    suspend fun deleteBook(@Path("endpoint") endpoint: String, @Path("id") id:Int): Response<Void>

    @GET("{endpoint}/{id}")
    suspend fun showBook(@Path("endpoint") endpoint: String, @Path("id") id: Int): Response<Data>
}