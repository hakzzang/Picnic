package hbs.com.picnic.view.recommend

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import hbs.com.picnic.R
import hbs.com.picnic.data.model.RecommendMenu
import hbs.com.picnic.ui.map.MapActivity
import hbs.com.picnic.utils.MenuType
import hbs.com.picnic.view.recommend.adapter.RecommendMenuAdapter
import hbs.com.picnic.view.recommend.presenter.RecommendMenuPresenter
import kotlinx.android.synthetic.main.view_recommend_menu.view.*

class RecommendMenuView : FrameLayout,
    RecommendMenuAdapter.MenuClickListener,
    RecommendMenuContract.View {

    /**Adapter에서 메뉴 클릭 시 호출*/
    override fun onMenuClick(type: MenuType) {
        menuPresenter.clickMenu(type)
    }

    private val menuPresenter by lazy {
        RecommendMenuPresenter(this, context.assets)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        menuPresenter.makeMenuList()
    }

    override fun goToListMap(locations:String) {
        Intent(context, MapActivity::class.java)
            .apply {
                putExtra("type", MapActivity.Type.LIST_MAP.value)
                putExtra("MENU_PARAMS", locations)
                context.startActivity(this)
            }
    }

    override fun setMenuList(menus:List<MenuType>) {
        val menuView = LayoutInflater.from(context).inflate(R.layout.view_recommend_menu, this)
        menuView.gv_menu.apply{
            adapter = RecommendMenuAdapter(context, menus, this@RecommendMenuView)
        }
    }
}
