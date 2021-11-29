/*
Author : Park Jong Seong
Created At : 2021-11-01 - 오후 10:21
Updated At :
Description : 레포지토리 정의
*/

package com.Demi.Move.repository

import android.app.Application
import android.util.Log
import com.Demi.Move.Database.ExtensionData
import com.Demi.Move.Database.PathData
import com.Demi.Move.Database.PathDataDao
import com.Demi.Move.Database.PathDataRoomDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class PathDataRepository(application: Application) {
    private var pathDataDao: PathDataDao?

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        val db: PathDataRoomDatabase? =
            PathDataRoomDatabase.getDatabase(application.applicationContext)
        pathDataDao = db!!.PathDataDao()
        Log.d("로그", "Repository Init")
    }

    fun insert(pathData: PathData) {
        coroutineScope.launch {
            pathDataDao!!.PathDataInsert(pathData)
        }
    }

    fun delete(pathData: PathData) {
        coroutineScope.launch {
            pathDataDao!!.PathdataDelete(pathData)
        }
    }

    fun update(pathData: PathData) {
        coroutineScope.launch {
            pathDataDao!!.PathDataUpdate(pathData)
        }
    }

    suspend fun getTypeData(type: String): List<String> =
        withContext(coroutineScope.coroutineContext) {
            pathDataDao!!.getExtensionListToPathData(type)
        }

    fun getAllData(): Flow<List<PathData>> = pathDataDao!!.getPathDataAll()

    fun insertExtensionData(vararg data: ExtensionData) {
        coroutineScope.launch {
            pathDataDao!!.ExtensionDataInsert(*data)
        }
    }

    fun deleteExtensionData(data: ExtensionData) {
        coroutineScope.launch {
            pathDataDao!!.ExtensionDataDelete(data)
        }
    }

    fun flowExtensionData(): Flow<List<ExtensionData>> = pathDataDao!!.test()

    companion object {
        private var instance:PathDataRepository? = null
        fun getInstance(application: Application):PathDataRepository {
            return instance ?: synchronized(this) {
                instance ?: PathDataRepository(application).also {
                    instance = it
                }
            }
        }
    }
}