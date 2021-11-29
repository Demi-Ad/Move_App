package com.Demi.Move.EditActivity

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.Demi.Move.repository.PathDataRepository
import kotlinx.coroutines.launch

class EditViewModel(application: Application) : AndroidViewModel(application) {
    private val repository  =  PathDataRepository.getInstance(application)

    private val extensions:MutableLiveData<List<String>> = MutableLiveData()
    val extension:LiveData<List<String>> = extensions

    fun getExtensions(type:String) {
        viewModelScope.launch {
            extensions.value = repository.getTypeData(type)
            Log.d("로그", "EditViewModel - getExtensions: ")
        }
    }

    fun clear() {
        extensions.value = emptyList()
    }
}