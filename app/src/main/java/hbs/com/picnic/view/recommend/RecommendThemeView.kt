package hbs.com.picnic.view.recommend

import android.content.Context
import android.location.Location
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import hbs.com.picnic.R
import hbs.com.picnic.data.model.ParkInfo
import hbs.com.picnic.ui.recommend.presenter.RecommendPresenter
import hbs.com.picnic.view.recommend.adapter.RecommendThemeAdapter
import hbs.com.picnic.view.recommend.presenter.RecommendThemePresenter
import kotlinx.android.synthetic.main.view_recommend_theme.view.*

class RecommendThemeView : FrameLayout,
    RecommendThemeContract.View {

    private var mMainPresenter :RecommendPresenter?=null

    private val snapHelper = LinearSnapHelper()

    private val themePresenter by lazy {
        RecommendThemePresenter(this, context.assets)
    }

    private val themeAdapter: RecommendThemeAdapter by lazy {
        RecommendThemeAdapter(themePresenter)
    }


    constructor(context: Context, currentLocation: Location) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init(){
        val themeView = LayoutInflater.from(context).inflate(R.layout.view_recommend_theme, this)
        themeView.rv_theme.apply{
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = themeAdapter
        }

        snapHelper.attachToRecyclerView(themeView.rv_theme)
    }

    fun updateTheme(currentLocation:Location, mainPresenter:RecommendPresenter){
        mMainPresenter = mainPresenter
        themePresenter.readFile(currentLocation)
    }

    override fun notifyDatas(themes: ArrayList<ParkInfo>) {
        themeAdapter.notifyDatas(themes)
    }

    override fun nearByThemes(lat:Double, lon:Double) {
        mMainPresenter?.getLocationInfo(
            coords = "${lon},${lat}",
            orders = "addr",
            output = "json"
        )
    }
}
