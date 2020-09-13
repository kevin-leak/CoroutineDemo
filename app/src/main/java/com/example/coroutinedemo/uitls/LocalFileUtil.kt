package com.example.coroutinedemo.uitls

import android.content.Context
import com.example.coroutinedemo.App
import java.io.File
import java.lang.StringBuilder
import java.util.*

class LocalFileUtil {
    private var context: Context = App.appContext
    private var paths: Paths = Paths()


    fun createUploadFile(fileName: String): File {
        var file = File(paths.uploadFolderPath, fileName)
        var dir = paths.uploadFolderPath
        while (file.exists()) {
            dir = "${paths.uploadFolderPath}${generateUUID()}${File.separator}"
            file = File(
                StringBuilder().append(dir)
                    .append(fileName).toString()
            )
        }
        File(dir).also { if (!it.exists()) it.mkdirs() }
        file.createNewFile()
        return file
    }

    fun getDownloadFilePath(): String {
        return paths.downLoadFolderPath
    }


    fun getFileDirectory(): File {
        return File(paths.fileDirPath)
    }

    private fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }

    inner class Paths {
        val fileDirPath: String by lazy {
            val filePath = StringBuilder(DEFAULT_CAPACITY)
                .append(context.filesDir.absolutePath)
                .append(File.separator)
                .append(File.separator)
                .toString()
            filePath.also { File(it).apply { if (!exists()) mkdirs() } }
        }
        val uploadFolderPath: String by lazy {
            val filePath = "$fileDirPath$UPLOAD_FOLDER${File.separator}"
            File(filePath).also {
                if (!it.exists()) {
                    it.mkdirs()
                }
            }
            filePath
        }
        val downLoadFolderPath: String by lazy {
            val filePath = "$fileDirPath$DOWNLOAD_FOLDER${File.separator}"
            File(filePath).also {
                if (!it.exists()) it.mkdirs()
            }
            filePath
        }
    }

    companion object {

        fun getLocalFile(url: String): File {
            return File(INSTANCE.getDownloadFilePath(), url)
        }

        private const val DEFAULT_CAPACITY = 64
        private const val UPLOAD_FOLDER = "UploadFolder"
        private const val DOWNLOAD_FOLDER = "DownloadFolder"

        @JvmStatic
        val INSTANCE: LocalFileUtil by lazy {
            LocalFileUtil()
        }
    }
}