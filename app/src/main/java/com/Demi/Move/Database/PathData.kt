/*
Author : Park Jong Seong
Created At : 2021-11-01 - 오후 6:46
Updated At : 
Description : 데이터 클래스 정의
*/

package com.Demi.Move.Database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * 데이터베이스에 저장될 테이블 및 컬럼정의
 * @param id 자동갱신
 * @param srcPath 옮겨질 폴더위치
 * @param dstPath 이동될 폴더위치
 * @param type 파일타입 Image,Video,Docs,Music
 *
 * [Parcelize] And [Parcelable] 직렬화를 위해 필요합니다
 *
 * [Entity] Room 라이브러리 테이블이름
 *
 * [PrimaryKey] 테이블의 기본키
 */

@Parcelize
@Entity(tableName = "data_table")
data class PathData(
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val srcPath: String,
    val dstPath: String,
    val actionType: String,
    val type: String
) : Parcelable

/**
 * View 에 보여주기위한 데이터 클래스
 * @param pathData
 * @param extensionList
 */

data class ViewPathData(
    val pathData: PathData,
    val extensionList: List<String>
)
