package com.example.coroutinedemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.coroutinedemo.net.DownLoader
import com.example.coroutinedemo.uitls.ApkInstallUtil
import com.example.coroutinedemo.uitls.LocalFileUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job


class MainActivity : BaseActivity(), DownLoadState {
    private lateinit var notification: Notification
    private lateinit var builder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    //    var url = "jingdutianxia5.2.2pj_2265.com.apk"
//    var url = "2020-9-7/983fdff24a73ad4G2RG4LYDCyhDeuA.apk"
//    var url = "F-Droid.apk"
    var url = "2020-8-31/cca9f0826f4be255tG5vAN22nX3Uj.apk"

    private val fileProcessJob: Job = Job()

    override fun getResId(): Int = R.layout.activity_main

    override fun onDestroy(): Unit = super.onDestroy().also { fileProcessJob.cancel() }

    fun onStartDownLoad(view: View) {
        DownLoader.downLoadWithProgress(fileProcessJob, url)
            .start {
                startState()
//                show(this, "file download", url)
            }.progress {
                tvProcess.text = it.toString()
//                builder.setProgress(100, (it * 100).toInt(), false)
//                notification = builder.build()
//                notificationManager.notify(1, notification)
            }.done {
                doneState()
            }.fail {
                failState()
                Toast.makeText(this@MainActivity, "fail", Toast.LENGTH_SHORT).show()
            }
    }

    override fun startState() {
        btnDownload.isClickable = false
        btnDownload.text = resources.getText(R.string.wait)
    }

    override fun progressState() {

    }

    override fun doneState() {
        btnDownload.isClickable = true
        btnInstall.visibility = View.VISIBLE
        notificationManager.cancel(1)
    }

    override fun failState() {
        btnDownload.isClickable = true
        btnInstall.visibility = View.GONE
        tvProcess.text = "0"
        btnDownload.text = resources.getText(R.string.start_download)
    }


    fun onStartInstallApk(view: View) {
        val file = LocalFileUtil.getLocalFile(url)
        Log.e("kyle-down", "onStartInstallApk: " + file.name + " " + file.length())
        ApkInstallUtil.installApk(file)
    }


    fun show(context: Context, title: String?, msg: String?) {
        notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel("id", "name", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(context, "id")
            builder.setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_download)
        } else {
            builder = NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_download)
        }
        builder.setProgress(100, 0, false)
        notification = builder.build()
        notificationManager.notify(1, builder.build())
    }

//    private fun intent(context: Context): Boolean {//判断应用的通知权限是否打开，返回Boolean值
//        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
//            val localIntent = Intent();
//            //判断API，跳转到应用通知管理页面
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0及以上
//                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS";
//                localIntent.data = Uri.fromParts("package", context.packageName, null);
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//8.0以下
//                localIntent.action = "android.settings.APP_NOTIFICATION_SETTINGS";
//                localIntent.putExtra("app_package", context.getPackageName());
//                localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
//            }
//            context.startActivity(localIntent);
//            return false
//        }
//        return true;
//    }
}

