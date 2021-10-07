package br.com.lno.workmanagertest.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import br.com.lno.workmanagertest.R
import br.com.lno.workmanagertest.databinding.ActivityMainBinding
import br.com.lno.workmanagertest.model.repository.RetrofitWorker
import br.com.lno.workmanagertest.view.adapter.QueueAdapter
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var queueAdapter = QueueAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvQueue.adapter = queueAdapter

        binding.btCreateRequest.setOnClickListener(this)
        binding.btPrune.setOnClickListener(this)

        val workQuery =
            WorkQuery.Builder.fromStates(
                listOf(
                    WorkInfo.State.ENQUEUED,
                    WorkInfo.State.RUNNING,
                    WorkInfo.State.FAILED,
                    WorkInfo.State.BLOCKED,
                    WorkInfo.State.CANCELLED
                )
            )
                .addTags(listOf("similar_job_tag")).build()

        // Observe all items that have not yet succeeded
        WorkManager.getInstance(this).getWorkInfosLiveData(workQuery).observe(this, {
            queueAdapter.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_cancel_all -> {
                WorkManager.getInstance(this).cancelAllWork()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.bt_create_request -> {
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
            R.id.bt_prune -> {
                WorkManager.getInstance(this).pruneWork()
            }
        }
    }
}