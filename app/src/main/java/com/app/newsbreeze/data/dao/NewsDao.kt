package com.app.newsbreeze.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.newsbreeze.data.entity.News
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news:News)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<News>)

    @Update
    suspend fun updateList(list: List<News>)

    @Update
    suspend fun update(news: News)

    @Query("DELETE FROM news_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM news_table")
    fun getAllNews(): Flow<List<News>>

    @Query("SELECT * FROM news_table WHERE Saved = 1")
    fun getAllSavedNews(): Flow<List<News>>

    @Query("SELECT * FROM news_table WHERE NewsID =:id ")
    fun getNewsById(id:Int): Flow<News>

    @Query("SELECT EXISTS (SELECT 1 FROM news_table WHERE Title=:title)")
    fun isTitleAdded(title: String): Int

}