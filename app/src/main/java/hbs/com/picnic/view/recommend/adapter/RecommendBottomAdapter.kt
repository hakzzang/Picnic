package hbs.com.picnic.view.recommend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import hbs.com.picnic.R
import hbs.com.picnic.data.model.RecommendBottom
import hbs.com.picnic.utils.CustomItemDecoration

open class RecommendBottomAdapter(val context: Context, val datas: ArrayList<RecommendBottom>) :
    RecyclerView.Adapter<RecommendBottomAdapter.RecommendViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder
    = RecommendViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_recommend_bottom,
            parent,
            false
        )
    )


    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    class RecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemSpace:Int by lazy { itemView.context.resources.getDimension(R.dimen.recommend_padding).toInt() }
        private val itemDecoration:CustomItemDecoration by lazy {
            CustomItemDecoration(RecyclerView.HORIZONTAL, itemSpace)
        }
        private val tvBottom:TextView = itemView.findViewById(R.id.tv_recommend_title)
        private val rvBottom:RecyclerView = itemView.findViewById(R.id.rv_recommend_bottom_info)

        fun bind(data: RecommendBottom) {
            PagerSnapHelper().run {
                attachToRecyclerView(rvBottom)
            }
            tvBottom.text = data.title
            rvBottom.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                addItemDecoration(itemDecoration)
                adapter = BottomInfoAdapter(context, data.datas)
            }
        }
    }
}

class BottomInfoAdapter(val context: Context, val datas: ArrayList<RecommendBottom.BottomInfo>) :
    RecyclerView.Adapter<BottomInfoAdapter.BottomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommend_bottom_info, parent, false)
        return BottomViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: BottomViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    class BottomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBottomTitle:TextView = itemView.findViewById(R.id.tv_bottom_title)
        val tvBottomTag:TextView = itemView.findViewById(R.id.tv_bottom_tag)

        fun bind(data: RecommendBottom.BottomInfo) {
            tvBottomTitle.text = data.title
            tvBottomTag.text = data.tags
        }
    }
}