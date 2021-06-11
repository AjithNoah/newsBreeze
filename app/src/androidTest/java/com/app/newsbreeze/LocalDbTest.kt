package com.app.newsbreeze


import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.app.newsbreeze.data.NewsDatabase
import com.app.newsbreeze.data.dao.NewsDao
import com.app.newsbreeze.data.entity.News
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalDbTest {

    private lateinit var newsDao: NewsDao
    private lateinit var db : NewsDatabase

    @Before
    fun createDb(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context,NewsDatabase::class.java).build()
        newsDao = db.newsDao()
    }

    @After
    fun closeDb(){
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertNews_withSample_returnOne(){
        val news = News("The Dark","Test data","Ajith","https://www.test.com",
            "https://www.example.com","23-06-2021","Test content","Test Name",0)

        runBlocking {
            newsDao.insert(news)
        }
        val isSaved = newsDao.isTitleAdded("The Dark")
        assertThat(isSaved, Is.`is`(1))
    }
    @Test
    @Throws(Exception::class)
    fun failCase_insertNews_withSample_returnZero(){
        val news = News("The Dark","Test data","Ajith","https://www.test.com",
            "https://www.example.com","23-06-2021","Test content","Test Name",0)

        runBlocking {
            newsDao.insert(news)
        }
        val isSaved = newsDao.isTitleAdded("The Moon")
        assertThat(isSaved, Is.`is`(0))
    }


    @Test
    @Throws(Exception::class)
    fun insertAndGetData() {
        val fakeList= mutableListOf<News>().apply {
            add(
                News("The Dark","Test data","Ajith","https://www.test.com",
                    "https://www.example.com","23-06-2021","Test content","Test Name",0)
            )
        }
        runBlocking {
            newsDao.insertList(fakeList)
            val allData = newsDao.getAllNews()
            assertThat(allData.first().size, Is.`is`(1))
        }

    }

}