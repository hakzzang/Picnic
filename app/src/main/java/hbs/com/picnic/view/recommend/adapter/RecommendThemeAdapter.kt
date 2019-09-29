package hbs.com.picnic.view.recommend.adapter

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hbs.com.picnic.R
import hbs.com.picnic.data.model.ParkInfo
import hbs.com.picnic.data.model.ThemeInfo
import hbs.com.picnic.view.recommend.presenter.RecommendThemePresenter

class RecommendThemeAdapter(val presenter: RecommendThemePresenter) :
    RecyclerView.Adapter<RecommendThemeAdapter.ThemeHolder>() {

    var datas: ArrayList<ParkInfo> = arrayListOf()

    fun notifyDatas(updateDatas: ArrayList<ParkInfo>) {
        datas.clear()
        datas.addAll(updateDatas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeHolder =
        ThemeHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_recommend_theme,
                parent,
                false
            )
        )


    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ThemeHolder, position: Int) {
        holder.bind(datas[position])
        holder.clContainer.setOnClickListener {
            presenter.clickTheme(datas[position].y1.toDouble(), datas[position].x1.toDouble())
        }
    }

    class ThemeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clContainer:ConstraintLayout = itemView.findViewById(R.id.cl_container)
        private val tvDistance: TextView = itemView.findViewById(R.id.tv_theme_distance)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_theme_title)
        private val tvAddr: TextView = itemView.findViewById(R.id.tv_theme_addr)
        private val ivTheme: ImageView = itemView.findViewById(R.id.iv_theme)

        fun bind(data: ParkInfo) {

            val themeLocation = Location("theme")
            themeLocation.apply {
                longitude = data.x1.toDouble()
                latitude = data.y1.toDouble()

              tvDistance.text = "${String.format("%.1f", data.distance)}km"
            }

            tvTitle.text = data.title
            tvAddr.text = data.addr

            Glide.with(itemView.context).load(data.img)
                .thumbnail(0.1f)
                .override(ivTheme.width, ivTheme.height).into(ivTheme)

        }
    }
}