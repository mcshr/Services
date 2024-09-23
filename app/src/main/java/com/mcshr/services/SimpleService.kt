package com.mcshr.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SimpleService: Service(){
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        log("onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            for(i in 0..100){
                delay(1000)
                log("timer:$i")
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        log("onDestroy")
        super.onDestroy()
    }

    private fun log(message:String){
        Log.d("SERVICE", message)
    }
    companion object{
        fun newIntent(context: Context): Intent {
            return Intent(context, SimpleService::class.java)
        }
    }
}