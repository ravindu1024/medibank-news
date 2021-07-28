package com.medibank.news.sources

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medibank.data.models.domain.NewsSource
import com.medibank.news.databinding.RowNewsSourceBinding

class SourcesAdapter(context: Context) : RecyclerView.Adapter<SourcesRowHolder>(){

    private val inflater = LayoutInflater.from(context)
    private val sourcesList = mutableListOf<NewsSource>()
    private lateinit var rowClickCallback: (String) -> Unit
    private lateinit var savedSourcesCallback: () -> List<String>

    fun setSources(list: List<NewsSource>){
        sourcesList.clear()
        sourcesList.addAll(list)
        notifyDataSetChanged()
    }

    fun setRowCallback(callback: (String) -> Unit){
        this.rowClickCallback = callback
    }

    fun setSavedSourcesCallback(callback: () -> List<String>){
        this.savedSourcesCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourcesRowHolder {
        val binding = RowNewsSourceBinding.inflate(inflater, parent, false)
        return SourcesRowHolder(binding) { source ->
            val v = sourcesList.first { it.id == source }
            this.rowClickCallback(source)

            // Re-draw the list with the updated saved items - we re-draw without fetching from
            // the api again. I agree that this looks a bit hacky and ideally should be
            // fetched and refreshed from a local datastore like Room or Realm
            val savedItems = savedSourcesCallback()
            v.saved = savedItems.contains(v.id)
            this@SourcesAdapter.notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: SourcesRowHolder, position: Int) {
        holder.bind(sourcesList[position])
    }

    override fun getItemCount() = sourcesList.size

}