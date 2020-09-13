package com.example.coroutinedemo

interface DownLoadState{
    fun  startState()
    fun  progressState()
    fun  doneState()
    fun  failState()
}