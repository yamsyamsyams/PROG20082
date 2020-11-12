package com.profjk.booksinfojson.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

/*
User: hoangjam
Name: Jameson Hoang
Student ID: 991548515
Date: 2020-11-12
*/

// baseURL must end with /
private var baseUrl = "https://www.googleapis.com/books/v1/"
// https://www.googleapis.com/books/v1/volumes?q=isbn:0738511633
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().baseUrl(baseUrl)
    .addConverterFactory(MoshiConverterFactory.create(moshi)).build()

interface BooksApiService {
//    @GET
//    suspend fun retrieveResponse() : Book

    @GET
    suspend fun retrieveResponse(@Url apiUrl : String) : Book // uses dynamic url
}

object BooksApi{
    val RETROFIT_SERVICE_BOOKS : BooksApiService by lazy {
        retrofit.create(BooksApiService::class.java)
    }
}