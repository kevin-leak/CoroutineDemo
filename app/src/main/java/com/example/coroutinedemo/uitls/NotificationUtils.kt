package com.example.coroutinedemo.uitls

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.coroutinedemo.R


object NotificationUtils {

    private lateinit var notification: Notification
    private lateinit var builder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    private fun isPermissionOpen(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat.from(context).importance != NotificationManager.IMPORTANCE_NONE
        } else NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    private fun openPermissionSetting(context: Context) {
        try {
            val localIntent = Intent()
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //直接跳转到应用通知设置的代码：
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localIntent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                context.startActivity(localIntent)
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                localIntent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                localIntent.putExtra("app_package", context.packageName)
                localIntent.putExtra("app_uid", context.applicationInfo.uid)
                context.startActivity(localIntent)
                return
            }
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                localIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                localIntent.addCategory(Intent.CATEGORY_DEFAULT)
                localIntent.data = Uri.parse("package:" + context.packageName)
                context.startActivity(localIntent)
                return
            }

            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                localIntent.data = Uri.fromParts("package", context.packageName, null)
                context.startActivity(localIntent)
                return
            }
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName(
                "com.android.settings",
                "com.android.setting.InstalledAppDetails"
            )
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
        } catch (e: Exception) {
            e.printStackTrace()
            println(" cxx   pushPermission 有问题")
        }
    }

    fun updateNotificationProgress(it: Float) {
        builder.setProgress(100, (it * 100).toInt(), false)
        notification = builder.build()
        notificationManager.notify(1, notification)
    }


    fun showNotification(context: Context, title: String?, msg: String?) {
        if (!isPermissionOpen(context)) {
            openPermissionSetting(context)
            return
        }
        notificationManager =
            context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel("id", "name", NotificationManager.IMPORTANCE_LOW)
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

    fun cancelNotification() {
        notificationManager.cancel(1)
    }
}