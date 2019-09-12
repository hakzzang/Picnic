package hbs.com.picnic.view.content

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import hbs.com.picnic.databinding.ItemContentSubtitleBinding
import kotlinx.android.synthetic.main.item_map.view.*


class ContentAdapter(private val contentMap: Map<String, ByteArray>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ContentViewType(val num: Int) {
        HEADER(0), CONTENT(1), HASHTAG(2), MAP(3), EMPTY(5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ContentViewType.HEADER.num -> {
                TitleViewHolder(makeInflater(parent, hbs.com.picnic.R.layout.item_content_title))
            }
            ContentViewType.CONTENT.num -> {
                ContentViewHolder(makeInflater(parent, hbs.com.picnic.R.layout.item_content_content))
            }
            ContentViewType.MAP.num -> {
                MapViewHolder(makeInflater(parent, hbs.com.picnic.R.layout.item_map))
            }
            ContentViewType.HASHTAG.num -> {
                SubTitleViewHolder(makeDataBinding(parent, hbs.com.picnic.R.layout.item_content_subtitle))
            }
            else -> {
                assert(false) { "절대 안 옴" }
                ContentViewHolder(makeInflater(parent, hbs.com.picnic.R.layout.item_content_subtitle))
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ContentViewType.HEADER.num
            1, 2 -> ContentViewType.CONTENT.num
            3 -> ContentViewType.MAP.num
            4 -> ContentViewType.HASHTAG.num
            else -> ContentViewType.EMPTY.num
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ContentViewType.HASHTAG.num) {
            setHashTag(holder)
        } else if (holder.itemViewType == ContentViewType.MAP.num) {
            if (contentMap.containsKey("MAP")) {
                setMapImage(holder, contentMap["MAP"])
            }
        }
    }

    inner class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class SubTitleViewHolder(val binding: ItemContentSubtitleBinding) : RecyclerView.ViewHolder(binding.root)
    inner class MapViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private fun makeInflater(parent: ViewGroup, layout: Int): View =
        LayoutInflater.from(parent.context).inflate(layout, parent, false)


    private fun <T : ItemContentSubtitleBinding> makeDataBinding(parent: ViewGroup, layout: Int) =
        DataBindingUtil.inflate<T>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )

    private fun provideRequestManager(context: Context) = Glide.with(context)

    private fun setHashTag(holder: RecyclerView.ViewHolder) {
        (holder as SubTitleViewHolder).apply {
            binding.rvContentFlex.apply {
                layoutManager = FlexboxLayoutManager(context).apply {
                    flexDirection = FlexDirection.ROW
                    justifyContent = JustifyContent.FLEX_START
                }
                adapter = ContentHashTagAdapter(
                    listOf(
                        "내 여자친구는",
                        "배가",
                        "고플것이다",
                        "새우버거는",
                        "응가를",
                        "했을 것이다.",
                        "응가~~"
                    )
                )
            }
        }
    }

    private fun setMapImage(holder: RecyclerView.ViewHolder, mapImage: ByteArray?) {
        val mapViewHolder = holder as MapViewHolder
        mapImage?.apply {
            val mapImageBitmap = BitmapFactory.decodeByteArray(mapImage, 0, mapImage.size)
            provideRequestManager(mapViewHolder.itemView.context)
                .load(mapImageBitmap)
                .into(mapViewHolder.itemView.iv_static_map)
        }
    }
}