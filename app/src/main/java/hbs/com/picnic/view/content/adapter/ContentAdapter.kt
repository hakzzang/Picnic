package hbs.com.picnic.view.content.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import hbs.com.picnic.R
import hbs.com.picnic.data.model.TourDetail
import hbs.com.picnic.data.model.TourInfo
import hbs.com.picnic.databinding.ItemContentContentBinding
import hbs.com.picnic.databinding.ItemContentSubcontentBinding
import hbs.com.picnic.databinding.ItemContentSubtitleBinding
import hbs.com.picnic.databinding.ItemContentTitleBinding
import hbs.com.picnic.ui.map.MapActivity
import kotlinx.android.synthetic.main.item_map.view.*


class ContentAdapter(
    private val tourItemInfo: TourInfo.TourItemInfo,
    private var tourDetail: TourDetail?,
    private val contentMap: Map<String, ByteArray>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val LIST_SIZE = 5
    enum class ContentViewType(val num: Int) {
        HEADER(0), CONTENT(1), SUB_CONTENT(2), MAP(3), HASHTAG(4), EMPTY(5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ContentViewType.HEADER.num -> {
                val itemContentTitleBinding = ItemContentTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TitleViewHolder(itemContentTitleBinding)
            }
            ContentViewType.CONTENT.num -> {
                val itemContentContentBinding = ItemContentContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ContentViewHolder(itemContentContentBinding).apply {
                    bindView()
                }
            }
            ContentViewType.SUB_CONTENT.num -> {
                val itemContentContentBinding = ItemContentSubcontentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SubContentViewHolder(itemContentContentBinding).apply {
                    bindView()
                }
            }
            ContentViewType.MAP.num -> {
                MapViewHolder(makeInflater(parent, hbs.com.picnic.R.layout.item_map)).apply {
                    bind(this)
                }
            }
            ContentViewType.HASHTAG.num -> {
                val itemContentSubTitleBinding = ItemContentSubtitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SubTitleViewHolder(itemContentSubTitleBinding)
            }
            else -> {
                assert(false) { "절대 안 옴" }
                MapViewHolder(makeInflater(parent, hbs.com.picnic.R.layout.item_map))
            }
        }
    }

    fun updateTourDetail() {
        notifyItemChanged(ContentViewType.CONTENT.num)
        notifyItemChanged(ContentViewType.SUB_CONTENT.num)
    }

    override fun getItemCount(): Int {
        return LIST_SIZE
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ContentViewType.HEADER.num
            1 -> ContentViewType.CONTENT.num
            2 -> ContentViewType.SUB_CONTENT.num
            3 -> ContentViewType.MAP.num
            4 -> ContentViewType.HASHTAG.num
            else -> ContentViewType.EMPTY.num
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ContentViewType.HEADER.num -> {
                val titleViewHolder = holder as TitleViewHolder
                titleViewHolder.bindView(tourItemInfo)
            }
            ContentViewType.CONTENT.num -> {
                val contentViewHolder = holder as ContentViewHolder
                contentViewHolder.bindView()
            }
            ContentViewType.HASHTAG.num -> setHashTag(holder)
            ContentViewType.MAP.num -> {
                val mapViewHolder = holder as MapViewHolder
            }
        }
    }

    inner class TitleViewHolder(private val itemContentTitleBinding: ItemContentTitleBinding) :
        RecyclerView.ViewHolder(itemContentTitleBinding.root) {
        fun bindView(tourItemInfo: TourInfo.TourItemInfo) {
            itemContentTitleBinding.title = tourItemInfo.title
            itemContentTitleBinding.tvContentTitle.isSelected = true
            itemContentTitleBinding.ivTelIcon.setOnClickListener {
                if(tourItemInfo.tel.isEmpty()){
                    val context = itemContentTitleBinding.root.context
                    Toast.makeText(context,
                        context.getString(R.string.toast_text_not_register_phone_number), Toast.LENGTH_SHORT).show()
                }else{
                    val telSplit = tourItemInfo.tel.split(",")
                    val telIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${telSplit[0]}"))
                    itemContentTitleBinding.ivTelIcon.context.startActivity(telIntent)
                }
            }
        }

    }

    inner class ContentViewHolder(private val binding: ItemContentContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView() {
            if (tourDetail == null) {
                binding.clContainer.visibility = View.INVISIBLE
                binding.lottieLoadingView.visibility = View.VISIBLE
                return
            }

            if (binding.tourDetail != this@ContentAdapter.tourDetail) {
                binding.clContainer.visibility = View.VISIBLE
                binding.lottieLoadingView.visibility = View.GONE
                binding.tourDetail = this@ContentAdapter.tourDetail
            }
        }
    }

    inner class SubContentViewHolder(private val binding: ItemContentSubcontentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView() {
            if (tourDetail == null) {
                binding.clContainer.visibility = View.INVISIBLE
                binding.lottieLoadingView.visibility = View.VISIBLE
                return
            }

            if (binding.tourDetail != this@ContentAdapter.tourDetail) {
                binding.clContainer.visibility = View.VISIBLE
                binding.lottieLoadingView.visibility = View.GONE
                binding.tourDetail = this@ContentAdapter.tourDetail
            }

        }
    }
    inner class SubTitleViewHolder(val binding: ItemContentSubtitleBinding) : RecyclerView.ViewHolder(binding.root)
    inner class MapViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(holder: RecyclerView.ViewHolder) {
            if (contentMap.containsKey("MAP")) {
                setMapImage(holder, contentMap["MAP"])
                holder.itemView.iv_static_map.visibility = View.VISIBLE
            }else{
                holder.itemView.iv_static_map.visibility = View.INVISIBLE
            }
            holder.itemView.setOnClickListener {
                Intent(it.context, MapActivity::class.java).apply {
                    putExtra("longitude", tourItemInfo.mapx)
                    putExtra("latitude", tourItemInfo.mapy)
                    it.context.startActivity(this)
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

    private fun makeInflater(parent: ViewGroup, layout: Int): View =
        LayoutInflater.from(parent.context).inflate(layout, parent, false)


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
}