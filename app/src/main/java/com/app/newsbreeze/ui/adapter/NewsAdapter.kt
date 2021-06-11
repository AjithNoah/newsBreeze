package com.app.newsbreeze.ui.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.app.newsbreeze.R
import com.app.newsbreeze.data.entity.News
import com.app.newsbreeze.databinding.ItemNewsBinding
import com.app.newsbreeze.util.Utils
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class NewsAdapter(val context: Context) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var list = emptyList<News>()
    var onItemClick: ((pos: String, model: News) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        val model = list[position]
        with(holder){

            binding.textTitle.text = model.Title
            binding.textContent.text = model.Description
            binding.textDate.text = Utils.apiTime(model.Date)
            Glide.with(context).load(model.Image).into(binding.imgBanner)
            if (model.Saved == 0){
                binding.btnSave.text = "Save"
                binding.rlBookmark.setBackgroundResource(R.drawable.ic_background)
                binding.imgBookMark.setImageResource(R.drawable.ic_save_outline)

            }else {
                binding.btnSave.text = "Saved"
                binding.rlBookmark.setBackgroundResource(R.drawable.corner_primary)
                binding.imgBookMark.setImageResource(R.drawable.ic_save)


            }
            binding.rlBookmark.setOnClickListener {
                onItemClick!!.invoke("Save", model)
            }
            binding.btnSave.setOnClickListener {
                onItemClick!!.invoke("Save", model)
            }
            binding.consParent.setOnClickListener {
                onItemClick!!.invoke("Read", model)
            }
            binding.btnRead.setOnClickListener {
                onItemClick!!.invoke("Read", model)

            }

        }
    }



    internal fun setNews(words: List<News>) {
        this.list = words
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemNewsBinding.bind(itemView)
    }

}