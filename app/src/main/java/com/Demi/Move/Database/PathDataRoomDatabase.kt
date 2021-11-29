/*
Author : Park Jong Seong
Created At : 2021-11-01 - 오후 7:38
Updated At :
Description : 데이터베이스 세팅
*/
package com.Demi.Move.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 *
 */

@Database(entities = [(PathData::class), (ExtensionData::class)], version = 1, exportSchema = false)
abstract class PathDataRoomDatabase : RoomDatabase() {
    abstract fun PathDataDao(): PathDataDao

    companion object {
        private var INSTANCE: PathDataRoomDatabase? = null

        internal fun getDatabase(context: Context): PathDataRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(PathDataRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            PathDataRoomDatabase::class.java,
                            "pathData_database"
                        ).addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                initData(context)
                            }
                            private fun initData(context: Context) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    getDatabase(context)?.PathDataDao()?.InitExtensionList(DataInit)
                                }
                            }
                        }).build()
                    }
                }
            }
            return INSTANCE
        }
    }

}

private val DataInit = arrayListOf(
    ExtensionData(type = "Image", extension = "bmp"),
    ExtensionData(type = "Image", extension = "gif"),
    ExtensionData(type = "Image", extension = "png"),
    ExtensionData(type = "Image", extension = "jpeg"),
    ExtensionData(type = "Music", extension = "flac"),
    ExtensionData(type = "Music", extension = "mid"),
    ExtensionData(type = "Music", extension = "mp3"),
    ExtensionData(type = "Music", extension = "wav"),
    ExtensionData(type = "Music", extension = "wma"),
    ExtensionData(type = "Video", extension = "avi"),
    ExtensionData(type = "Video", extension = "flv"),
    ExtensionData(type = "Video", extension = "mp4"),
    ExtensionData(type = "Video", extension = "mkv"),
    ExtensionData(type = "Docs", extension = "txt"),
    ExtensionData(type = "Docs", extension = "doc"),
    ExtensionData(type = "Docs", extension = "docx"),
    ExtensionData(type = "Docs", extension = "hwp"),
    ExtensionData(type = "Docs", extension = "ppt"),
    ExtensionData(type = "Docs", extension = "pptx"),
    ExtensionData(type = "Docs", extension = "pdf")
)