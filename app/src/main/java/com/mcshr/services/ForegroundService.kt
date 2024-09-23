package com.mcshr.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForegroundService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        log("onCreate")
        super.onCreate()
        createChannel()
        val notification = createNotification()
        startForeground(ID_NOTIFICATION, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            for (i in 0..10) {
                delay(1000)
                log("timer:$i")
            }
            stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        log("onDestroy")
        super.onDestroy()
    }

    private fun log(message: String) {
        Log.d("SERVICE", "Foreground service - $message")
    }

    private fun createNotification() = NotificationCompat.Builder(this, ID_CHANNEL_FOREGROUND)
        .setContentTitle("Foreground Service")
        .setContentText("service is working now...")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .build()

    private fun createChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ID_CHANNEL_FOREGROUND,
                NAME_CHANNEL_FOREGROUND,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val ID_CHANNEL_FOREGROUND = "id_notify_foregroundService"
        private const val NAME_CHANNEL_FOREGROUND = "Foreground Service"
        private const val ID_NOTIFICATION = 777

        fun newIntent(context: Context): Intent {

            return Intent(context, ForegroundService::class.java)
        }
    }
}