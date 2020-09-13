package com.example.coroutinedemo

interface DownLoadState {
    fun startState()
    fun progressState(process: Float)
    fun doneState()
    fun failState()
}