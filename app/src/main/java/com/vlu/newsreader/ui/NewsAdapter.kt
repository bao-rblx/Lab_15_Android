package com.vlu.newsreader.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vlu.newsreader.databinding.ItemNewsBinding
import com.vlu.newsreader.model.NewsItem

class NewsAdapter : ListAdapter<NewsItem, NewsAdapter.NewsViewHolder>(DIFF) {

    class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NewsItem) {
            binding.tvMeta.text = "#${item.id} • User ${item.userId}"
            binding.tvTitle.text = item.title
            binding.tvBody.text = item.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(a: NewsItem, b: NewsItem) = a.id == b.id
            override fun areContentsTheSame(a: NewsItem, b: NewsItem) = a == b
        }
    }
}
