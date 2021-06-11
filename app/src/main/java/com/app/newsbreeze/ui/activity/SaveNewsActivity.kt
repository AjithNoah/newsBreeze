package com.app.newsbreeze.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.newsbreeze.R
import com.app.newsbreeze.data.entity.News
import com.app.newsbreeze.databinding.ActivityNewsBinding
import com.app.newsbreeze.databinding.ActivitySaveNewsBinding
import com.app.newsbreeze.databinding.LayoutFilterBinding
import com.app.newsbreeze.service.Status
import com.app.newsbreeze.ui.adapter.NewsAdapter
import com.app.newsbreeze.ui.adapter.SaveNewsAdapter
import com.app.newsbreeze.util.viewBinding
import com.app.newsbreeze.viewmodel.NewsViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveNewsActivity : AppCompatActivity() {

    var saveNewsAdapter:SaveNewsAdapter? = null
    var newsList:ArrayList<News> = ArrayList()
    var tempList:ArrayList<News> = ArrayList()
    val binding: ActivitySaveNewsBinding by viewBinding()
    private val newsViewModel: NewsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_news)
        initViews()
        getSavedNews()
        setOnClickListener()
    }

    //getting local saved news
    private fun getSavedNews() {
        newsViewModel.getSavedNews().observe(this, Observer {
            it?.let {
                when(it.status){
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        if (it.data!!.isNotEmpty()){
                            newsList.clear()
                            newsList.addAll(it.data)
                            saveNewsAdapter!!.setNews(newsList)
                            binding.textNodata.visibility = View.GONE

                        }
                        else {
                            binding.textNodata.visibility = View.VISIBLE
                        }
                    }
                    Status.ERROR -> {
                        binding.textNodata.visibility = View.VISIBLE
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun setOnClickListener() {
        saveNewsAdapter!!.onItemClick = {newsId ->
            val intent = Intent(this,NewsDetailActivity::class.java)
            intent.putExtra("newsId",newsId)
            startActivity(intent)
        }

        binding.imgBackSaved.setOnClickListener {
            onBackPressed()
        }

        binding.edtSearchSave.addTextChangedListener(object : TextWatcher {
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
                saveNewsAdapter!!.setNews(sortList)
            }
            1-> {
                sortList.sortWith { p0, p1 -> p1!!.Date.compareTo(p0!!.Date) }
                saveNewsAdapter!!.setNews(sortList)
            }
            2-> {
                saveNewsAdapter!!.setNews(newsList)
            }
        }
    }

    private fun filterList(txt: String) {
        tempList.clear()
        if (!TextUtils.isEmpty(txt) && newsList.isNotEmpty()) {
            val searchText = txt.toLowerCase()
            for (items in newsList) {
                val video = items.Title.toLowerCase()
                if (video.contains(searchText)) {
                    tempList.add(items)
                }
            }
            saveNewsAdapter?.setNews(tempList)
        } else {
            tempList.clear()
            saveNewsAdapter?.setNews(newsList)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun initViews() {
        binding.recycleSaved.layoutManager = LinearLayoutManager(this)
        saveNewsAdapter = SaveNewsAdapter(this)
        binding.recycleSaved.adapter = saveNewsAdapter
    }
}