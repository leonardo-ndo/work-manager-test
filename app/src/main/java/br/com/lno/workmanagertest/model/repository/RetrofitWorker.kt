package br.com.lno.workmanagertest.model.repository

import android.content.Context
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
                    Result.success()
                }
                response.code() in 500..599 -> {
                    Result.retry()
                }
                else -> {
                    Result.failure()
                }
            }

        } catch (e: UnknownHostException) {
            Result.retry()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}