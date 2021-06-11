package com.app.newsbreeze.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.newsbreeze.data.dao.NewsDao
import com.app.newsbreeze.data.entity.News

@Database(entities = [News::class],version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao():NewsDao
}