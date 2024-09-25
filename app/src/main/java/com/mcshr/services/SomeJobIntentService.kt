@file:Suppress("DEPRECATION")

package com.mcshr.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class SomeJobIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        val page = intent.getIntExtra(PAGE, 0)
        for (i in 0..10) {
            Thread.sleep(1000)
            log("timer:$i for page:$page")
        }
    }

    override fun onCreate() {
        log("onCreate")
        super.onCreate()
    }

    override fun onDestroy() {
        log("onDestroy")
        super.onDestroy()
    }

    private fun log(message: String) {
        Log.d("SERVICE", "Intent service - $message")
    }

    companion object {
        private const val PAGE = "page"
        private const val ID = 123
        fun enqueque(context: Context, page: Int) {
            enqueueWork(
                context,
                SomeJobIntentService::class.java,
                ID,
                newIntent(context, page)

            )
        }
        private fun newIntent(context: Context, page: Int):Intent{
            return Intent(context, SomeJobIntentService::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}