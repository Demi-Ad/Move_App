/*
Author : Park Jong Seong
Created At : 2021-11-01 - 오후 10:06
Updated At :
Description : 메인 액티비티 뷰 모델
*/
package com.Demi.Move.MainActivityResource

import android.app.Application
import androidx.lifecycle.*
import com.Demi.Move.Database.PathData
import com.Demi.Move.Database.ViewPathData
import com.Demi.Move.repository.PathDataRepository
import kotlinx.coroutines.launch

/**
 * [allData] 데이터베이스에서 "data_table" 테이블에 모든 데이터를 받는다
 *
 * [viewAllData] 리사이클러뷰에 표현 해주기 위해 라이브데이터 변형
 */

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PathDataRepository = PathDataRepository.getInstance(application)

    private val allData: LiveData<List<PathData>> = repository.getAllData().asLiveData()

    val viewAllData = allData.switchMap { data ->
        liveData {
            val arr = ArrayList<ViewPathData>()
            data.forEach {
                arr.add(ViewPathData(it, repository.getTypeData(it.type)))
            }
            emit(arr)
        }
    }


    fun insert(pathData: PathData) = viewModelScope.launch {
        repository.insert(pathData)
    }

    fun delete(pathData: PathData) = viewModelScope.launch {
        repository.delete(pathData)
    }

    fun update(pathData: PathData) = viewModelScope.launch {
        repository.update(pathData)
    }

}