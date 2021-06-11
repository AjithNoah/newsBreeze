package com.app.newsbreeze.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.app.newsbreeze.R
import com.app.newsbreeze.data.entity.News
import com.app.newsbreeze.databinding.ActivityNewsBinding
import com.app.newsbreeze.databinding.ActivityNewsDetailBinding
import com.app.newsbreeze.service.Status
import com.app.newsbreeze.ui.adapter.NewsAdapter
import com.app.newsbreeze.util.viewBinding
import com.app.newsbreeze.viewmodel.NewsViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity() {

    val binding: ActivityNewsDetailBinding by viewBinding()
    private val newsViewModel: NewsViewModel by viewModels()
    var newsId = 0
    var news:News? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        initViews()
        setOnclickListener()
    }

    private fun setOnclickListener() {
        binding.imgBackDetail.setOnClickListener {
            onBackPressed()
        }
        binding.btnSaved.setOnClickListener {
            saveFunction()
        }
        binding.imgBookMark.setOnClickListener {
            saveFunction()
        }
    }

    private fun saveFunction() {
        val status = if(news?.Saved == 0){
            1
        } else {
            0
        }

        val news = news
        news?.Saved = status
        newsViewModel.update(news!!)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun initViews() {
        val intent = intent
        if (intent!=null){
            newsId = intent.getIntExtra("newsId",0)
        }

        getNewsFromLocal(newsId)
    }

    private fun getNewsFromLocal(newsId: Int) {
        newsViewModel.getNewsById(newsId).observe(this, Observer {
            it?.let {
                when(it.status){
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        if (it.data!=null){
                            news = it.data
                            binding.collapsingToolbar.title = it.data.Title
                            binding.textContent.text = it.data.Content
                            binding.textAuthor.text = it.data.Author
                            if (it.data.Saved == 0){
                                binding.btnSaved.text = "Save"
                                binding.imgBookMark.setImageResource(R.drawable.ic_save_outline)


                            }else {
                                binding.btnSaved.text = "Saved"
                                binding.imgBookMark.setImageResource(R.drawable.ic_save)
                            }
                            Glide.with(this).load(it.data.Image).into(binding.imgBanner)

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}