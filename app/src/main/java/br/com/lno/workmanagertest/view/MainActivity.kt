package br.com.lno.workmanagertest.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import br.com.lno.workmanagertest.databinding.ActivityMainBinding
import br.com.lno.workmanagertest.model.repository.RetrofitWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btCreateRequest.setOnClickListener {

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
    }
}