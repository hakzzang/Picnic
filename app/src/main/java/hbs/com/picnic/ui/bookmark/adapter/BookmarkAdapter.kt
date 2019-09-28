package hbs.com.picnic.ui.bookmark.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hbs.com.picnic.data.model.Bookmark
import hbs.com.picnic.databinding.ItemScrapBinding


class BookmarkAdapter : ListAdapter<Bookmark, RecyclerView.ViewHolder>(BookmarkDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemScrapBinding = ItemScrapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScrapViewHolder(itemScrapBinding)
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItem(position)) {
            is Bookmark -> {
                val scrapViewHolder = holder as ScrapViewHolder
                scrapViewHolder.bindView(getItem(position))
            }
        }
    }

    inner class ScrapViewHolder(private val binding: ItemScrapBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(bookmark: Bookmark) {
            binding.bookmark = bookmark
        }
    }

    class BookmarkDiffUtil : DiffUtil.ItemCallback<Bookmark>() {
        override fun areItemsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean = oldItem.equals(newItem)
    }
}