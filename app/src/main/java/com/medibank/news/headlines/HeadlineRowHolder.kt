package com.medibank.news.headlines

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medibank.data.models.domain.NewsHeadline
import com.medibank.news.R
import com.medibank.news.databinding.RowNewsHeadlineBinding

class HeadlineRowHolder(private val binding: RowNewsHeadlineBinding, private val callback: (NewsHeadline) -> Unit) : RecyclerView.ViewHolder(binding.root){

    fun bind(row: NewsHeadline){
        Glide.with(binding.root)
            .load(row.urlToImage)
            .into(binding.imageView)

        binding.textTitle.text = row.title
        binding.textDescription.text = row.description
        binding.textAuthor.text = binding.root.context.getString(R.string.headline_author, row.author, row.sourceName)

        binding.textDescription.visibility = if(row.description.isEmpty()) View.GONE else View.VISIBLE

        binding.root.tag = row
        binding.root.setOnClickListener { v ->
            callback(v.tag as NewsHeadline)
        }
    }
}