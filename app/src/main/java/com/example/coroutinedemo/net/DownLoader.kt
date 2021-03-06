package com.example.coroutinedemo.net

import android.util.Log
import com.example.coroutinedemo.uitls.LocalFileUtil
import com.example.coroutinedemo.uitls.ToastUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception
import kotlin.reflect.KSuspendFunction1 as SuspendFun

object DownLoader {

    private lateinit var uiScope: CoroutineScope

    private var start: (() -> Unit)? = null
    private var progress: ((Float) -> Unit)? = null
    private var done: (() -> Unit)? = null
    private var fail: (() -> Unit)? = null

    private lateinit var fileProcessJob: Job

    fun fail(back: () -> Unit): DownLoader {
        fail = back
        return this
    }

    fun progress(back: (Float) -> Unit): DownLoader {
        progress = back
        return this
    }

    fun start(back: () -> Unit): DownLoader {
        start = back
        return this
    }

    fun done(back: () -> Unit): DownLoader {
        done = back
        return this
    }

    fun downLoadWithProgress(job: Job, url: String): DownLoader {
        fileProcessJob = job
        uiScope = CoroutineScope(Dispatchers.Main + fileProcessJob)
        uiScope.launch {
            start?.invoke()
            flow { downLoadFileFromNet(url, this::emit) }
                .flowOn(Dispatchers.IO)
                .collect { progress?.invoke(it) }
        }
        return this
    }

    private suspend fun downLoadFileFromNet(url: String, emitProgress: SuspendFun<Float, Unit>) {
        val response = NetService.getInstance().downloadFile(url).execute()
        if (!response.isSuccessful) dealFail().also { return }
        response.body()?.use {
            val contentLength = it.contentLength()
            try {
                val inStream: InputStream = it.byteStream()
                val file = LocalFileUtil.getLocalFile(url)
                val outStream: FileOutputStream = file.outputStream()
                var currentLength = 0L

                val buff = ByteArray(1024)
                var len: Int = inStream.read(buff)
                var percent: Float = -0.1f

                while (len != -1) {
                    outStream.write(buff, 0, len)
                    currentLength += len
                    val tmp: Float = currentLength * 1.0F / contentLength
                    if (percent < tmp) {
                        percent = tmp
                        emitProgress(percent)
                    }
                    len = inStream.read(buff)
                }
            } catch (e: Exception) {
                dealFail().also { return }
            }
            withContext(Dispatchers.Main) { done?.invoke() }
        }
    }

    private suspend fun dealFail() {
        withContext(Dispatchers.Main) { fail?.invoke() }
        cancel()
    }

    private fun cancel() {
        if (fileProcessJob.isActive) fileProcessJob.cancel()
    }

}

