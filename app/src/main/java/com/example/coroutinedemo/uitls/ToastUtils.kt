package com.example.coroutinedemo.uitls

import android.widget.Toast
import com.example.coroutinedemo.App

object ToastUtils {
    fun showText(txt: String) {
        Toast.makeText(App.appContext, txt, Toast.LENGTH_SHORT).show()
    }
}