package com.example.coroutinedemo

import android.view.View
import android.widget.Toast
import com.example.coroutinedemo.net.DownLoader
import com.example.coroutinedemo.uitls.ApkInstallUtil
import com.example.coroutinedemo.uitls.LocalFileUtil
import com.example.coroutinedemo.uitls.NotificationUtils
import com.example.coroutinedemo.uitls.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job


class MainActivity : BaseActivity(), DownLoadState {
    //    var url = "jingdutianxia5.2.2pj_2265.com.apk"
    //    var url = "2020-9-7/983fdff24a73ad4G2RG4LYDCyhDeuA.apk"
    //    var url = "moonreaderprowmpjmodzsb.apk"
    var url = "F-Droid.apk"

    private val fileProcessJob: Job = Job()

    override fun getResId(): Int = R.layout.activity_main

    override fun onDestroy() {
        super.onDestroy()
        fileProcessJob.cancel()
        NotificationUtils.cancelNotification()
    }

    fun onStartDownLoad(view: View) {
        DownLoader.downLoadWithProgress(fileProcessJob, url)
            .start {
                startState()
                NotificationUtils.showNotification(this, "file download", url)
            }.progress {
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
        btnInstall.visibility = View.VISIBLE
        NotificationUtils.cancelNotification()
    }

    override fun failState() {
        btnDownload.isClickable = true
        btnInstall.visibility = View.GONE
        tvProcess.text = "0"
        btnDownload.text = resources.getText(R.string.start_download)
    }


    fun onStartInstallApk(view: View) {
        val file = LocalFileUtil.getLocalFile(url)
        ApkInstallUtil.installApk(file)
    }
}

