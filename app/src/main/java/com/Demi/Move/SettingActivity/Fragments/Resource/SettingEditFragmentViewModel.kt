package com.Demi.Move.SettingActivity.Fragments.Resource

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.Demi.Move.Database.ExtensionData
import com.Demi.Move.Database.ExtensionView
import com.Demi.Move.repository.PathDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingEditFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PathDataRepository.getInstance(application)


    val extensionData = repository.flowExtensionData().asLiveData()
    val extensionView = extensionData.switchMap { data ->
        liveData {
            val temp = data.groupBy { it.type }
                .map { ExtensionView(it.key, it.value) } as ArrayList
            emit(temp)
        }
    }


    fun insert(data: ExtensionData) = viewModelScope.launch {
        repository.insertExtensionData(data)
    }

    fun delete(data: ExtensionData) = viewModelScope.launch {
        repository.deleteExtensionData(data)
    }

    private suspend fun test(data: ExtensionData): Boolean {
        val a =
            withContext(Dispatchers.Default) {
                extensionData.asFlow().toList().flatten()
            }
        return data in a
    }
}