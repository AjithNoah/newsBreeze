package com.app.newsbreeze.di

import android.app.Application
import androidx.room.Room
import com.app.newsbreeze.data.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): NewsDatabase =
        Room.databaseBuilder(app, NewsDatabase::class.java, "news_database")
            .fallbackToDestructiveMigration()
            .build()


    @Provides
    fun provideNewsDao(db: NewsDatabase) = db.newsDao()

}