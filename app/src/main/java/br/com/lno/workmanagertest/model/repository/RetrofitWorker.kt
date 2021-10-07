package br.com.lno.workmanagertest.model.repository

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.UnknownHostException

class RetrofitWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.nasa.gov")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(NasaService::class.java)

    override suspend fun doWork(): Result {

        return try {

            val response = service.list()

            when {
                response.isSuccessful -> {
                    Log.d(javaClass.simpleName, "Task $id was successful")
                    Result.success()
                }
                response.code() in 500..599 -> {
                    Log.e(
                        javaClass.simpleName,
                        "Task $id was returned error 5XX. Retrying: ${response.message()}"
                    )
                    Result.retry()
                }
                else -> {
                    Log.e(javaClass.simpleName, "Task $id has failed: ${response.message()}")
                    Result.failure()
                }
            }

        } catch (e: UnknownHostException) {
            Log.e(javaClass.simpleName, "Task $id has failed: ${e.message}", e)
            Result.retry()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Task $id has failed: ${e.message}", e)
            Result.failure()
        }
    }
}