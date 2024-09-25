package com.mcshr.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mcshr.services.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.simpleService.setOnClickListener {
            startService(SimpleService.newIntent(this))
        }
        binding.foregroundService.setOnClickListener {
            ContextCompat.startForegroundService(this, ForegroundService.newIntent(this))
        }
        binding.intentService.setOnClickListener {
            ContextCompat.startForegroundService(this, SomeIntentService.newIntent(this))
        }
        binding.jobScheduler.setOnClickListener {
            startJobService()
        }
        binding.jobIntentService.setOnClickListener{
            SomeJobIntentService.enqueque(this, page++)
        }
    }

    private fun startJobService() {
        val componentName = ComponentName(this, SomeJobService::class.java)
        val jobInfo = JobInfo.Builder(SomeJobService.ID, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            .build()
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        //jobScheduler.schedule(jobInfo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = SomeJobService.newIntent(page++)
            jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
        } else {
            ContextCompat.startForegroundService(
                this,
                SomeIntentService.newIntentExtra(this, page++)
            )
        }
    }

    private fun showNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                ID_CHANNEL_NOTIFICATION_SERVICE1,
                NAME_CHANNEL_NOTIFICATION_SERVICE1,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = NotificationCompat.Builder(this, ID_CHANNEL_NOTIFICATION_SERVICE1)
            .setContentTitle("Service 1")
            .setContentText("hi-hi ha-ha he-he")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        notificationManager.notify(1, notification)
    }

    companion object {
        private const val ID_CHANNEL_NOTIFICATION_SERVICE1 = "id1"
        private const val NAME_CHANNEL_NOTIFICATION_SERVICE1 = "Service №1"
    }

}