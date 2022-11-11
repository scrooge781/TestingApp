package com.jun.android.developer.data

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/prod")
    suspend fun getResponse(): Response<ItemModel>
}