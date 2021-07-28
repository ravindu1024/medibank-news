package com.medibank.news.sources

import androidx.recyclerview.widget.RecyclerView
import com.medibank.data.models.domain.NewsSource
import com.medibank.news.databinding.RowNewsSourceBinding

class SourcesRowHolder(private val binding: RowNewsSourceBinding, private val callback: (String) -> Unit): RecyclerView.ViewHolder(binding.root) {

    fun bind(row: NewsSource){
        binding.textName.text = row.name
        binding.textDescription.text = row.description
        binding.checkBox.isChecked = row.saved

        binding.root.tag = row.id
        binding.root.setOnClickListener { v ->
            callback(v.tag as String)
        }
    }
}