package com.jun.android.developer.business

import com.jun.android.developer.data.ApiService
import javax.inject.Inject

class NetworkUseCase @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getResponse() = apiService.getResponse()
}