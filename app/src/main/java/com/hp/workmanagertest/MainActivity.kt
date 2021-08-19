package com.hp.workmanagertest

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.bt_create_request).setOnClickListener {

            val retrofitRequest = OneTimeWorkRequestBuilder<RetrofitWorker>()
//                .setConstraints(
//                    Constraints.Builder()
//                        .setRequiredNetworkType(NetworkType.CONNECTED)
//                        .build()
//                )
//                .setBackoffCriteria(
//                    BackoffPolicy.EXPONENTIAL,
//                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
//                    TimeUnit.MILLISECONDS
//                )
                .keepResultsForAtLeast(24, TimeUnit.HOURS)
                .addTag("similar_job_tag")
                .build()

            WorkManager.getInstance(this).enqueue(retrofitRequest)

        }

        WorkManager.getInstance(this).getWorkInfosByTag("similar_job_tag")
            .addListener(() { Toast.makeText(this, "", Toast.LENGTH_LONG).show() })
    }
}