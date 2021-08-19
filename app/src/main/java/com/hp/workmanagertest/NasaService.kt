package com.hp.workmanagertest

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaService {

    @GET("/EPIC/api/natural")
    suspend fun list(@Query("api_key") apiKey: String = "DEMO_KEY"): Response<String>

}