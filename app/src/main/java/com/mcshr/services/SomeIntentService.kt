@file:Suppress("DEPRECATION")

package com.mcshr.services

import android.app.IntentService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class SomeIntentService() : IntentService("SomeIntentService") {

    @Deprecated("Deprecated in Java")
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {
        val page = intent?.getIntExtra(PAGE, 0)
        for (i in 0..10) {
            Thread.sleep(1000)
            log("timer:$i for page:$page")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreate() {
        log("onCreate")
        super.onCreate()
        setIntentRedelivery(true)
        createChannel()
        val notification = createNotification()
        startForeground(ID_NOTIFICATION, notification)
    }


    @Deprecated("Deprecated in Java")
    override fun onDestroy() {
        log("onDestroy")
        super.onDestroy()
    }

    private fun log(message: String) {
        Log.d("SERVICE", "Intent service - $message")
    }

    private fun createNotification() = NotificationCompat.Builder(this, ID_CHANNEL_FOREGROUND)
        .setContentTitle("Intent Service")
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
        private const val ID_CHANNEL_FOREGROUND = "id_notify_intentService"
        private const val NAME_CHANNEL_FOREGROUND = "Intent Service"
        private const val ID_NOTIFICATION = 776
        private const val PAGE = "page"

        fun newIntent(context: Context): Intent {
            return Intent(context, SomeIntentService::class.java).apply {
            }
        }
        fun newIntentExtra(context: Context, page: Int):Intent{
            return Intent(context, SomeIntentService::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}