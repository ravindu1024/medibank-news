package com.medibank.news.headlines

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medibank.data.models.domain.NewsHeadline
import com.medibank.news.databinding.RowNewsHeadlineBinding

class NewsHeadlineAdapter(context: Context) : RecyclerView.Adapter<HeadlineRowHolder>() {
    private val inflater = LayoutInflater.from(context)
    private val newsList = mutableListOf<NewsHeadline>()
    private lateinit var callback: (NewsHeadline) -> Unit

    fun setCallback(callback: (NewsHeadline) -> Unit) {
        this.callback = callback
    }

    fun setHeadlines(headlines: List<NewsHeadline>) {
        newsList.clear()
        newsList.addAll(headlines)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineRowHolder {
        val binding = RowNewsHeadlineBinding.inflate(inflater, parent, false)
        return HeadlineRowHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: HeadlineRowHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount() = newsList.size
}