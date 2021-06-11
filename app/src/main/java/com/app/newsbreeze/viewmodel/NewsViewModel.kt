package com.app.newsbreeze.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.app.newsbreeze.data.dao.NewsDao
import com.app.newsbreeze.data.entity.News
import com.app.newsbreeze.data.repo.NewsRepository
import com.app.newsbreeze.service.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsViewModel @ViewModelInject constructor(private val newsRepository: NewsRepository,private val newsDao: NewsDao):ViewModel() {

    fun getNews() = liveData<Resource<List<News>>> {
        newsRepository.getNews()
            .onStart {
                emit(Resource.loading(data = null))
            }
            .catch {
                emit(Resource.error(data = null, msg = "Cannot reach server..try again"))
            }
            .collect {
                emit(Resource.success(it))
            }

    }

    fun getNewsById(id:Int) = liveData<Resource<News>> {
        newsRepository.getNewsById(id)
            .onStart {
                emit(Resource.loading(data = null))
            }
            .catch {
                emit(Resource.error(data = null, msg = "Cannot reach server..try again"))
            }
            .collect {
                emit(Resource.success(it))
            }

    }
    fun getSavedNews() = liveData<Resource<List<News>>> {
        newsRepository.getSavedNews()
            .onStart {
                emit(Resource.loading(data = null))
            }
            .catch {
                emit(Resource.error(data = null, msg = "Cannot reach server..try again"))
            }
            .collect {
                emit(Resource.success(it))
            }

    }

    fun update(news: News){
        viewModelScope.launch {
            newsRepository.update(news)
        }
    }


}