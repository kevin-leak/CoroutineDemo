package com.example.coroutinedemo

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.coroutinedemo.uitls.NotificationUtils


abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1

        private val PERMISSIONS_STORAGE: Array<String> = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getResId())
        val permission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)
        }
    }

    abstract fun getResId(): Int

}