package com.example.coroutinedemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.example.coroutinedemo.net.DownLoader
import com.example.coroutinedemo.net.NetService
import com.example.coroutinedemo.uitls.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job


class MainActivity : BaseActivity(), DownLoadState {
    private var url = "F-Droid.apk"

    private val fileProcessJob: Job = Job()

    override fun getResId(): Int = R.layout.activity_main

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        etUrl.setText( NetService.baseUrl + url)
        etUrl.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        fileProcessJob.cancel()
        NotificationUtils.cancelNotification()
    }

    fun onStartDownLoad(view: View) {
        DownLoader.downLoadWithProgress(fileProcessJob, url)
            .start {
                startState()
                NotificationUtils.showNotification(this, null, url)
            }.progress {
                progressState(it)
                NotificationUtils.updateNotificationProgress(it)
            }.done {
                doneState()
                ToastUtils.showText("Download Success")
            }.fail {
                failState()
                ToastUtils.showText("Download fail")
            }
    }


    override fun startState() {
        btnDownload.isClickable = false
        btnDownload.text = resources.getText(R.string.wait)
    }

    override fun progressState(process: Float) {
        tvProcess.text = process.toString()
    }

    override fun doneState() {
        btnDownload.isClickable = true
        cvInstall.visibility = View.VISIBLE
        tvFileName.text = UrlUtils.getFileNameFromUrl(url)
        NotificationUtils.cancelNotification()
    }

    override fun failState() {
        btnDownload.isClickable = true
        cvInstall.visibility = View.GONE
        tvProcess.text = "0"
        btnDownload.text = resources.getText(R.string.start_download)
    }


    fun onStartInstallApk(view: View) {
        val file = LocalFileUtil.getLocalFile(url)
        ApkInstallUtil.installApk(file)
    }
}

