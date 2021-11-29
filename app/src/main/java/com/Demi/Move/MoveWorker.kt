package com.Demi.Move

import android.content.Context
import android.media.MediaScannerConnection
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.Demi.Move.Utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files

class MoveWorker(val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        try {
            val filesPath = inputData.keyValueMap[Constant.FILE_PATH] as Array<String>
            val dstPath = inputData.keyValueMap[Constant.DST_PATH] as String?
            val action = inputData.keyValueMap[Constant.ACTION] as String

            val files = filesPath.map { File(it) }
            val toRootFolder = files.first().path.split(files.first().name)[0]

            Log.d("로그", "MoveWorker - doWork: ${File(toRootFolder).isDirectory}")

            when (action) {
                Constant.MOVE -> {
                    withContext(Dispatchers.IO) {
                        files.forEach {
                            val newFile = File("$dstPath/${it.name}")
                            try {
                                Files.copy(it.toPath(), newFile.toPath())
                            } catch (e: Exception) {
                                val reNameFile = recursiveFileName(newFile)
                                Files.copy(it.toPath(), reNameFile.toPath())
                            }
                            it.delete()
                        }
                    }
                }
                Constant.DEL -> {
                    withContext(Dispatchers.IO) {
                        files.forEach {
                            it.delete()
                        }
                    }
                }
                Constant.COPY -> {
                    withContext(Dispatchers.IO) {
                        files.forEach {
                            val newFile = File("$dstPath/${it.name}")
                            try {
                                Files.copy(it.toPath(), newFile.toPath())
                            } catch (e: Exception) {
                                val reNameFile = recursiveFileName(newFile)
                                Files.copy(it.toPath(), reNameFile.toPath())
                            }
                        }
                    }
                }
            }

            MediaScannerConnection.scanFile(
                applicationContext,
                arrayOf(toRootFolder,dstPath?:""),
                null,
                null
            )

        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
        return Result.success()
    }

    private tailrec fun recursiveFileName(file: File): File {
        val filename = file.name.split(".${file.extension}")[0]
        Log.d("로그", "MoveWorker - recursiveFileName: $filename")

        var randChar = ""

        repeat(5) {
            randChar += (97..122).random().toChar()
        }
        val reNameFile = "$filename-$randChar.${file.extension}"
        val newFile = File(file.path.split(file.name)[0] + reNameFile)
        return if (!newFile.exists()) return newFile else recursiveFileName(newFile)
    }
}