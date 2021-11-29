/*
Author : Park Jong Seong
Created At : 2021-11-01 - 오후 7:26
Updated At :
Description : 데이터베이스 DAO
*/

package com.Demi.Move.Database

import androidx.room.*
import kotlinx.coroutines.flow.Flow


/**
 *
 */

@Dao
interface PathDataDao {
    @Query("SELECT * FROM data_table ORDER BY id ASC")
    fun getPathDataAll(): Flow<List<PathData>>

    @Query("SELECT extension FROM extension_list WHERE type=:type")
    fun getExtensionListToPathData(type: String): List<String>

    @Insert
    fun PathDataInsert(vararg data: PathData)

    @Delete
    fun PathdataDelete(data: PathData)

    @Update
    fun PathDataUpdate(data: PathData)

    @Insert
    fun InitExtensionList(data: ArrayList<ExtensionData>)

    @Insert
    fun ExtensionDataInsert(vararg data:ExtensionData)

    @Delete
    fun ExtensionDataDelete(data:ExtensionData)

    @Query("select distinct type from extension_list")
    fun getExtensionType():List<String>

    @Query("SELECT * FROM extension_list WHERE type=:type")
    fun getExtensionTypes(type:String):List<ExtensionData>

    @Query("select * from extension_list")
    fun test(): Flow<List<ExtensionData>>

}