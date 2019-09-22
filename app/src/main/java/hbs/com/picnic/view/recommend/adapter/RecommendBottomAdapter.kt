package hbs.com.picnic.view.recommend.adapter

import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import hbs.com.picnic.R
import hbs.com.picnic.content.ContentActivity
import hbs.com.picnic.data.model.TourInfo
import hbs.com.picnic.utils.CustomItemDecoration

open class RecommendBottomAdapter(val context: Context, var datas: ArrayList<TourInfo>, var currentLocation: Location) :
    RecyclerView.Adapter<RecommendBottomAdapter.RecommendViewHolder>() {

    fun notifyDatas(updateDatas: ArrayList<TourInfo>, location: Location) {
        currentLocation = location
        datas.clear()
        datas = updateDatas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder = RecommendViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_recommend_bottom,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.bind(datas[position], currentLocation)
    }

    class RecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemSpace: Int by lazy {
            itemView.context.resources.getDimension(R.dimen.recommend_padding).toInt()
        }
        private val itemDecoration: CustomItemDecoration by lazy {
            CustomItemDecoration(RecyclerView.HORIZONTAL, itemSpace)
        }
        private val tvBottom: TextView = itemView.findViewById(R.id.tv_recommend_title)
        private val rvBottom: RecyclerView = itemView.findViewById(R.id.rv_recommend_bottom_info)

        fun bind(data: TourInfo, currentLocation: Location) {
            tvBottom.text = data.title
            rvBottom.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                addItemDecoration(itemDecoration)
                adapter = data.datas?.let { BottomInfoAdapter(context, it, currentLocation) }
            }
            rvBottom.onFlingListener = null
            PagerSnapHelper().run {
                attachToRecyclerView(rvBottom)
            }
        }
    }
}

class BottomInfoAdapter(
    val context: Context,
    val datas: ArrayList<TourInfo.TourItemInfo>,
    val currentLocation: Location
) :
    RecyclerView.Adapter<BottomInfoAdapter.BottomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommend_bottom_info, parent, false)
        return BottomViewHolder(view, currentLocation)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: BottomViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    class BottomViewHolder(val view: View, private val currentLocation: Location) : RecyclerView.ViewHolder(view) {
        val clContainer:ConstraintLayout = view.findViewById(R.id.cl_container)
        val tvBottomTitle: TextView = view.findViewById(R.id.tv_bottom_title)
        val tvBottomDist: TextView = view.findViewById(R.id.tv_bottom_distance)
        val tvBottomTag: TextView = view.findViewById(R.id.tv_bottom_tag)
        val ivBottomImg: ImageView = view.findViewById(R.id.iv_bottom_img)

        fun bind(data: TourInfo.TourItemInfo) {
            val target = Location("target")
            target.apply {
                longitude = data.mapx
                latitude = data.mapy
                tvBottomDist.text = "${String.format("%.1f", getDistance(this))}km"
            }

            tvBottomTitle.text = data.title
            tvBottomTag.text = data.tel
            Glide.with(view.context).load(data.firstimage)
                .override(ivBottomImg.width, ivBottomImg.height).into(ivBottomImg)

            Log.d("BottomViewHolder", Gson().toJson(data))
            clContainer.setOnClickListener {
                Intent(view.context, ContentActivity::class.java).apply{
                    putExtra("data", Gson().toJson(data))
                    view.context.startActivity(this)
                }
            }
        }

        private fun getDistance(target: Location): Float = target.distanceTo(currentLocation) / 1000
    }

}