package hbs.com.picnic.view.content.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hbs.com.picnic.R
import kotlinx.android.synthetic.main.item_subtitle.view.*

class ContentHashTagAdapter(private val hashTags:List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_subtitle, parent, false)
        return HomeCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hashTags.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HomeCategoryViewHolder).apply {
            val content = hashTags[position].replace(" ","_")
            itemView.btn_hash_tag.text = "#$content"
        }
    }

    inner class HomeCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view)
}