package com.jun.android.developer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jun.android.developer.business.DataStorageUseCase
import com.jun.android.developer.business.NetworkUseCase
import com.jun.android.developer.data.ItemModel
import com.jun.android.developer.util.Constants
import com.jun.android.developer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkUseCase: NetworkUseCase,
    private val dataStorageUseCase: DataStorageUseCase
) : ViewModel() {

    private val _responseItem = MutableLiveData<Resource<ItemModel>>()
    val responseItem: LiveData<Resource<ItemModel>> get() = _responseItem

    private val _itemDataStorage = MutableLiveData<String>()
    val itemDataStorage: LiveData<String> get() = _itemDataStorage

    private val _firstWebStartStr = MutableLiveData<String>()
    val firstWebStartStr: LiveData<String> get() = _firstWebStartStr

    fun getResponse() {
        viewModelScope.launch {
            _responseItem.postValue(Resource.loading(null))

            networkUseCase.getResponse().let { response ->
                when (response.isSuccessful) {
                    true -> _responseItem.postValue(Resource.success(response.body()))
                    else -> _responseItem.postValue(Resource.error("Ooops...", null))
                }
            }
        }
    }

    fun getDataStorage(key: String) {
        _itemDataStorage.postValue(dataStorageUseCase.getDataStorage(key))
    }

    fun saveDateStorage(response: ItemModel) {
        dataStorageUseCase.saveDataStorage(Constants.HOME, response.home)
        dataStorageUseCase.saveDataStorage(Constants.LINK, response.link)
        _firstWebStartStr.postValue(response.link)
    }
}