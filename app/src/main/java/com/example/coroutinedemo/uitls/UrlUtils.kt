package com.example.coroutinedemo.uitls

import java.io.File

object UrlUtils {
    private const val httpString = "http://"
    private const val httpsString = "https://"
    private const val endNameApk = ".apk"

    fun isApkUrl(url: String): Boolean {
        return if (url.startsWith(httpString) || url.startsWith(httpsString)) {
            url.endsWith(endNameApk)
        } else {
            false
        }
    }

    fun getFileNameFromUrl(url: String): String {
        val index = url.lastIndexOf('/')
        return url.substring(index + 1, url.length)
    }

    fun getRequestUrl(url: String): String {
        var baseUrl: String = url
        if (url.startsWith(httpString)) baseUrl = url.substring(httpString.length, url.length)
        if (url.startsWith(httpsString)) baseUrl = url.substring(httpsString.length, url.length)
        return baseUrl
    }

}