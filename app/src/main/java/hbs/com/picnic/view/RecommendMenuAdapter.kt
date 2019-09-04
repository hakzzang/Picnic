package hbs.com.picnic.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import hbs.com.picnic.R
import hbs.com.picnic.data.model.RecommendMenu
import kotlinx.android.synthetic.main.item_recommend_row.view.*

class RecommendMenuAdapter(private val context: Context, private val menus: List<RecommendMenu>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recommend_row, parent, false)

        Glide.with(context).load(menus[position].icon).into(view.iv_menu_icon)
        view.tv_menu_title.text = menus[position].title
        return view
    }

    override fun getItem(position: Int): Any = menus[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = menus.size
}