package com.app.newsbreeze.data.repo

import android.text.TextUtils
import androidx.lifecycle.asLiveData
import com.app.newsbreeze.data.dao.NewsDao
import com.app.newsbreeze.data.entity.News
import com.app.newsbreeze.service.ApiService
import com.app.newsbreeze.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class NewsRepository @Inject constructor(private val apiService: ApiService,private val newsDao: NewsDao) {

    val dataee = newsDao.getAllNews()
    val localList:ArrayList<News> = ArrayList()

    suspend fun getNews():Flow<List<News>>{
         refreshNews().collect {
             if (it.isNotEmpty()){
                 newsDao.deleteAll()
                 newsDao.insertList(it)
             }
         }
        return newsDao.getAllNews()
    }

    fun getNewsById(id:Int):Flow<News>{
        return newsDao.getNewsById(id)
    }

    fun getSavedNews():Flow<List<News>>{
        return newsDao.getAllSavedNews()
    }


    suspend fun update(news: News){
        newsDao.update(news)
    }


    private fun refreshNews(): Flow<List<News>> {
        return flow {
            if (Utils.isOnline()){
                val response = apiService.newsData()
                val list:ArrayList<News> = ArrayList()
                localList.addAll(dataee.first())
                if (response.status == Utils.STATUS_OK){
                    if (response.articles!!.isNotEmpty()){
                        for (data in response.articles){
                            var isAvailable = false
                            var saveStatus = 0
                            if (localList.isNotEmpty()){
                                for (dat in localList){
                                    if (dat.Title == data.title){
                                        saveStatus = dat.Saved
                                        isAvailable = true
                                        break
                                    }
                                }
                            }
                            if (!isAvailable){
                                list.add(News(data.title,data.description!!,data.author!!,checkNull(data.urlToImage.toString()),data.url!!,data.publishedAt!!,data.content!!,data.source.name,0))

                            }else{
                                list.add(News(data.title,data.description!!,data.author!!,checkNull(data.urlToImage.toString()),data.url!!,data.publishedAt!!,data.content!!,data.source.name,saveStatus))

                            }

                        }
                    }

                }
                emit(list)
            }else {
                emit(newsDao.getAllNews().first())
            }



        }.flowOn(Dispatchers.IO)
    }

    private fun checkNull(data:String):String{
        return if (TextUtils.isEmpty(data)){
            ""
        } else {
            data
        }
    }

}