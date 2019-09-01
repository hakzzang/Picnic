package hbs.com.picnic.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import hbs.com.picnic.R
import hbs.com.picnic.databinding.ItemContentSubtitleBinding

class ContentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ContentViewType(val num: Int) {
        HEADER(0), CONTENT(1), HASHTAG(2), EMPTY(5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ContentViewType.HEADER.num -> {
                TitleViewHolder(makeInflater(parent, R.layout.item_content_title))
            }
            ContentViewType.CONTENT.num -> {
                ContentViewHolder(makeInflater(parent, R.layout.item_content_content))
            }
            ContentViewType.HASHTAG.num -> {
                SubTitleViewHolder(makeDataBinding(parent, R.layout.item_content_subtitle))
            }
            else -> {
                assert(false) { "절대 안 옴" }
                ContentViewHolder(makeInflater(parent, R.layout.item_content_subtitle))
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ContentViewType.HEADER.num
            1, 2, 3 -> ContentViewType.CONTENT.num
            4 -> ContentViewType.HASHTAG.num
            else -> ContentViewType.EMPTY.num
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ContentViewType.HASHTAG.num) {
            (holder as SubTitleViewHolder).apply {
                binding.rvContentFlex.apply {
                    layoutManager = FlexboxLayoutManager(context).apply {
                        flexDirection = FlexDirection.ROW
                        justifyContent = JustifyContent.FLEX_START
                    }
                    adapter = ContentHashTagAdapter(listOf("내 여자친구는", "배가", "고플것이다", "새우버거는", "응가를", "했을 것이다.", "응가~~"))
                }
            }
        }
    }

    inner class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class SubTitleViewHolder(val binding: ItemContentSubtitleBinding) : RecyclerView.ViewHolder(binding.root)


    private fun makeInflater(parent: ViewGroup, layout: Int): View =
        LayoutInflater.from(parent.context).inflate(layout, parent, false)

    private fun <T : ItemContentSubtitleBinding> makeDataBinding(parent: ViewGroup, layout: Int) =
        DataBindingUtil.inflate<T>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
}