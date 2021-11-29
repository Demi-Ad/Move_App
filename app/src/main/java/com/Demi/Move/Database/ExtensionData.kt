/*
Author : Park Jong Seong
Created At : 2021-11-01 - 오후 7:22
Updated At : 
Description : 확장자가 저장되는 테이블
*/
package com.Demi.Move.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 */

@Entity(tableName = "extension_list")
data class ExtensionData(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    val type:String,
    val extension:String
)

data class ExtensionView(
    val type:String,
    val extensions: List<ExtensionData>
)