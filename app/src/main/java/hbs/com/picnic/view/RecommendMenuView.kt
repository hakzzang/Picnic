package hbs.com.picnic.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import hbs.com.picnic.R
import hbs.com.picnic.data.model.RecommendMenu
import kotlinx.android.synthetic.main.view_recommend_menu.view.*

class RecommendMenuView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializedView()
    }

    private fun initializedView() {
        val menuView = LayoutInflater.from(context).inflate(R.layout.view_recommend_menu, this)
        menuView.gv_menu.apply{
            adapter = RecommendMenuAdapter(context, getMenus())
        }
    }


    private fun getMenus(): List<RecommendMenu> =
        arrayListOf(
            RecommendMenu(0, "한복체험", R.drawable.ic_hanbok_line),
            RecommendMenu(1, "한옥나들이", R.drawable.ic_palace_black_line),
            RecommendMenu(2, "떡카페", R.drawable.ic_ricecake_line),
            RecommendMenu(3, "키즈카페", R.drawable.ic_lego_line),
            RecommendMenu(4, "푸드트럭", R.drawable.ic_foodtruck_line),
            RecommendMenu(5, "아이스크림", R.drawable.ic_ice_line)
        )
}
