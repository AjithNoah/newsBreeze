package com.app.newsbreeze.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.newsbreeze.R
import com.app.newsbreeze.data.entity.News
import com.app.newsbreeze.databinding.ActivityNewsBinding
import com.app.newsbreeze.databinding.LayoutFilterBinding
import com.app.newsbreeze.service.Status
import com.app.newsbreeze.ui.adapter.NewsAdapter
import com.app.newsbreeze.util.viewBinding
import com.app.newsbreeze.viewmodel.NewsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    val binding: ActivityNewsBinding by viewBinding()
    var newsAdapter:NewsAdapter? = null
    var newsList:ArrayList<News> = ArrayList()
    var tempList:ArrayList<News> = ArrayList()
    private val newsViewModel: NewsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        initViews()
        setOnClickListener()
        getNews()

    }

    private fun getNews() {
        newsViewModel.getNews().observe(this, Observer {
            it?.let {
                when(it.status){
                    Status.LOADING -> {
                        showProgress()
                    }
                    Status.SUCCESS -> {
                        dismissProgress()
                        if (it.data!!.isNotEmpty()){
                            binding.textNodata.visibility = View.GONE

                            newsList.clear()
                            newsList.addAll(it.data)
                            newsAdapter!!.setNews(newsList)
                        }
                        else {
                            binding.textNodata.visibility = View.VISIBLE
                            //Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        binding.textNodata.visibility = View.VISIBLE
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun setOnClickListener() {
        newsAdapter!!.onItemClick = {isFrom,model ->
            if (isFrom == "Save"){
                val status = if(model.Saved == 0){
                    1
                } else {
                    0
                }

                val news = model
                news.Saved = status
                newsViewModel.update(news)
            }
            else {
                val intent = Intent(this,NewsDetailActivity::class.java)
                intent.putExtra("newsId",model.NewsID)
                startActivity(intent)
            }

        }
        binding.imgSaved.setOnClickListener {
            startActivity(Intent(this,SaveNewsActivity::class.java))

        }
        binding.edtSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val txt = p0.toString()
                filterList(txt)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.imgFilter.setOnClickListener {
            filterDialog()
        }
    }

    private fun filterDialog() {
        val fromSheetDialog = BottomSheetDialog(this)

        val view  = this.layoutInflater.inflate(R.layout.layout_filter, null)
        fromSheetDialog.setContentView(view)
        val sheetContainer = view.parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        val bind = LayoutFilterBinding.bind(view)
        bind.rlAsc.setOnClickListener {
            sortList(0)
            fromSheetDialog.dismiss()
        }
        bind.rlDesc.setOnClickListener {
            sortList(1)
            fromSheetDialog.dismiss()

        }
        bind.textPopularity.setOnClickListener {
            sortList(2)
            fromSheetDialog.dismiss()

        }

        fromSheetDialog.show()

    }

    private fun sortList(input: Int) {
        val sortList:ArrayList<News> = ArrayList()
        sortList.addAll(newsList)
        when(input){
            0 -> {
                sortList.sortWith { p0, p1 -> p0!!.Date.compareTo(p1!!.Date)}
                newsAdapter!!.setNews(sortList)
            }
            1-> {
                sortList.sortWith { p0, p1 -> p1!!.Date.compareTo(p0!!.Date) }
                newsAdapter!!.setNews(sortList)
            }
            2-> {
                newsAdapter!!.setNews(newsList)
            }
        }
    }

    private fun filterList(txt: String) {
        tempList.clear()
        if (!TextUtils.isEmpty(txt)) {
            val searchText = txt.toLowerCase()
            for (items in newsList) {
                val video = items.Title.toLowerCase()
                if (video.contains(searchText)) {
                    tempList.add(items)
                }
            }
            newsAdapter?.setNews(tempList)
        } else {
            tempList.clear()
            newsAdapter?.setNews(newsList)
        }
    }

    private fun initViews() {
        binding.recycleNews.layoutManager = LinearLayoutManager(this)
        newsAdapter = NewsAdapter(this)
        binding.recycleNews.adapter = newsAdapter
    }

    fun showProgress(){
        binding.progressLoad.visibility = View.VISIBLE
    }
    fun dismissProgress(){
        binding.progressLoad.visibility = View.GONE
    }
}