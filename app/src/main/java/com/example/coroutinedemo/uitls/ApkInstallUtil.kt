package com.example.coroutinedemo.uitls

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.example.coroutinedemo.App
import java.io.File

object ApkInstallUtil {
    private val packageName: String = App.appContext.packageName

    fun installApk(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val apkUri: Uri =
                FileProvider.getUriForFile(App.appContext, "$packageName.fileprovider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            val uri: Uri = Uri.fromFile(file)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
        }
        App.appContext.startActivity(intent)
    }
}