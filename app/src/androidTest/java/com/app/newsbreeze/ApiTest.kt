package com.app.newsbreeze

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.newsbreeze.service.ApiService
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class ApiTest {

    private lateinit var apiService: ApiService

    @Before
    fun createService(){
        val client = OkHttpClient()
        val retrofit  = Retrofit.Builder()
            .baseUrl(BuildConfig.APP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    @Test
    @Throws(IOException::class)
    fun newsTestSuccess(){
        runBlocking {
            val request = apiService.newsData()
            val response = request.status
            assertEquals("ok",response)
        }

    }

}