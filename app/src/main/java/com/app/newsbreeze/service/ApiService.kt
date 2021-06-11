package com.app.newsbreeze.service

import com.app.newsbreeze.model.NewsResponse
import com.app.newsbreeze.util.Utils
import retrofit2.http.*

interface ApiService {

    @GET("v2/everything?q=apple")
    suspend fun newsData(
        @Query("from") from :String = "2021-06-05",
        @Query("to") to :String = Utils.currentDate(),
        @Query("sortBy") sort :String = "popularity",
        @Query("apiKey") key :String = Utils.APIKEY
    ):NewsResponse


}