package com.jun.android.developer.business

import android.content.SharedPreferences
import javax.inject.Inject

class DataStorageUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun saveDataStorage(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getDataStorage(key: String): String? {
        return sharedPreferences.getString(key, "")
    }
}