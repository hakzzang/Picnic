package hbs.com.picnic.ui.recommend

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hbs.com.picnic.R
import hbs.com.picnic.ui.map.MapActivity
import hbs.com.picnic.ui.recommend.presenter.RecommendPresenter
import hbs.com.picnic.utils.CustomItemDecoration
import hbs.com.picnic.view.recommend.adapter.RecommendBottomAdapter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.Gravity
import androidx.drawerlayout.widget.DrawerLayout
import com.kakao.util.helper.Utility.getPackageInfo
import hbs.com.picnic.data.model.TourInfo
import hbs.com.picnic.data.model.TourRequest
import hbs.com.picnic.data.remote.TourAPI
import hbs.com.picnic.ui.bookmark.BookmarkActivity
import hbs.com.picnic.utils.SharedManager
import hbs.com.picnic.utils.TourType
import hbs.com.picnic.utils.XmlParser
import kotlinx.android.synthetic.main.activity_recommend.*
import kotlinx.android.synthetic.main.content_drawer.*
import kotlinx.android.synthetic.main.content_drawer_menu.*
import kotlinx.android.synthetic.main.content_recommend.*
import org.json.JSONObject
import java.security.MessageDigest
import kotlin.math.sign

class RecommendActivity : AppCompatActivity(), RecommendContract.View, View.OnClickListener {

    val REQUEST_SELECT_CODE: Int = 1002

    private val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    private val recommendPresenter by lazy { RecommendPresenter(this, locationManager) }

    private val bottomAdapter: RecommendBottomAdapter by lazy {
        RecommendBottomAdapter(
            this@RecommendActivity,
            currentLocation
        )
    }

    private val itemDecoration: CustomItemDecoration by lazy {
        CustomItemDecoration(
            RecyclerView.VERTICAL,
            resources.getDimension(R.dimen.recommend_bottom_space).toInt()
        )
    }

    private var currentLong: Double = 0.0
    private var currentLat: Double = 0.0
    private var currentLocation: Location = Location("current")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        setClickListener()

        dl_recommend.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerClosed(drawerView: View) {
                val requestList = arrayListOf<Int>()

                TourType.values().filter {
                    SharedManager.getBoolean(this@RecommendActivity, it.name)
                }.map {
                    requestList.add(it.value)
                }

                lottie_loading.visibility = View.VISIBLE

                recommendPresenter.updateTourInfo(requestList, currentLong, currentLat)
            }

            override fun onDrawerOpened(drawerView: View) {
                setSelection()
            }
        })

        rv_recommend_bottom.apply {
            layoutManager = LinearLayoutManager(this@RecommendActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(itemDecoration as RecyclerView.ItemDecoration)
            adapter = bottomAdapter
        }

        recommendPresenter.getGpsInfo(this@RecommendActivity)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_recommend_map -> {
                Intent(this@RecommendActivity, MapActivity::class.java)
                    .apply {
                        putExtra("longitude", currentLong)
                        putExtra("latitude", currentLat)
                        putExtra("type", MapActivity.Type.SELECT_MAP.value)
                        startActivityForResult(this, REQUEST_SELECT_CODE)
                    }
            }

            R.id.iv_gps -> {
                recommendPresenter.getGpsInfo(this@RecommendActivity)
            }
            R.id.iv_menu -> dl_recommend.openDrawer(Gravity.RIGHT)
            R.id.iv_drawer_close, R.id.iv_drawer_home -> dl_recommend.closeDrawer(Gravity.RIGHT)
            R.id.ll_bookmark_container ->
                Intent(this, BookmarkActivity::class.java).apply {
                    startActivity(this)
                }

            R.id.cl_drawer -> {
            }

            R.id.menu_food -> Toast.makeText(this@RecommendActivity, "맛집은 기본이죠~", Toast.LENGTH_SHORT).show()
            R.id.menu_culture, R.id.menu_festival, R.id.menu_shopping,
            R.id.menu_sports, R.id.menu_travel, R.id.menu_tour -> {
                v.isSelected = !v.isSelected
                SharedManager.putBoolean(
                    this@RecommendActivity,
                    convertStringFrom(v?.id).name, v.isSelected
                )
            }

        }
    }

    /** 현재 위치 주소 업데이트 및 Tour 데이터 가져오기 **/
    override fun updateLocation(name: String) {
        tv_recommend_map.text = getLocationFrom(name)

        val requestList = arrayListOf<Int>()

        TourType.values().filter {
            SharedManager.getBoolean(this@RecommendActivity, it.name) || it.name == TourType.FOOD.name
        }.map {
            requestList.add(it.value)
        }


        lottie_loading.visibility = View.VISIBLE
        recommendPresenter.updateTourInfo(requestList, currentLong, currentLat)

        reco_theme_view.updateTheme(currentLocation, recommendPresenter)

    }

    /** 하단 Tour 리스트 업데이트 **/
    override fun updateTourInfo(datas: List<String>) {
        val bottomDatas = arrayListOf<TourInfo>()

        datas.map {
            bottomDatas.add(XmlParser.tourParser(it))
        }
        lottie_loading.visibility = View.GONE

        bottomAdapter.notifyDatas(bottomDatas, currentLocation)
    }

    /** 권한 허용 안하고 다시 GPS 값을 구해올 때 **/
    override fun showGPSDialogAgain() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            .apply {
                setTitle(resources.getString(R.string.app_name))
                setMessage(resources.getString(R.string.permission_message))
                    .setPositiveButton(resources.getString(R.string.go_to_setting)) { _, _ ->
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + context.packageName)
                        )
                            .apply {
                                addCategory(Intent.CATEGORY_DEFAULT)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(this)
                            }
                    }
                    .setNegativeButton(resources.getString(R.string.text_cancel)) { dialog, _ -> dialog?.dismiss() }
            }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_CODE) {
            data?.let { location ->
                recommendPresenter.getLocationInfo(
                    coords = "${location.getDoubleExtra("longitude", 0.0)}," +
                            "${location.getDoubleExtra("latitude", 0.0)}",
                    orders = "addr",
                    output = "json"
                )
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == recommendPresenter.REQUEST_PERMISSION_LOCATION) {
            var check_result = true

            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {
                recommendPresenter.getGpsInfo(this@RecommendActivity)

            } else {
                Toast.makeText(this, resources.getString(R.string.permission_message), Toast.LENGTH_LONG).show()

            }
        }
    }

    /** JSON String 값으로 주소 이름 받아오기 **/
    private fun getLocationFrom(jsonString: String): String {
        var resultString = "위치 정보를 불러오지 못했습니다. 다시 시도해주세요."
        val jsonObject = JSONObject(jsonString)
        with(jsonObject) {
            val statusCode = getJSONObject("status").getString("code")
            if (statusCode == "3") return resultString
            val regionObject = getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("region")

            with(regionObject) {
                resultString =
                    "${getJSONObject("area1").getString("name")} " +
                            "${getJSONObject("area2").getString("name")} " +
                            "${getJSONObject("area3").getString("name")}"

                val coordsObj = getJSONObject("area3")
                    .getJSONObject("coords")
                    .getJSONObject("center")

                currentLong = coordsObj.getDouble("x")
                currentLat = coordsObj.getDouble("y")
                currentLocation.apply {
                    longitude = currentLong
                    latitude = currentLat
                }
            }
        }
        return resultString
    }

    override fun onPause() {
        super.onPause()
        recommendPresenter.onPause()
    }

    override fun onBackPressed() {
        if (dl_recommend.isDrawerOpen(Gravity.RIGHT)) {
            dl_recommend.closeDrawer(Gravity.RIGHT)
        } else
            super.onBackPressed()
    }


    private fun setSelection() {
        SharedManager.putBoolean(this@RecommendActivity, TourType.FOOD.name, true)
        menu_festival.isSelected =
            SharedManager.getBoolean(this@RecommendActivity, convertStringFrom(menu_festival.id).name)
        menu_travel.isSelected =
            SharedManager.getBoolean(this@RecommendActivity, convertStringFrom(menu_travel.id).name)
        menu_tour.isSelected =
            SharedManager.getBoolean(this@RecommendActivity, convertStringFrom(menu_tour.id).name)
        menu_sports.isSelected =
            SharedManager.getBoolean(this@RecommendActivity, convertStringFrom(menu_sports.id).name)
        menu_shopping.isSelected =
            SharedManager.getBoolean(this@RecommendActivity, convertStringFrom(menu_shopping.id).name)
        menu_culture.isSelected =
            SharedManager.getBoolean(this@RecommendActivity, convertStringFrom(menu_culture.id).name)
    }

    /** OnClickListener 설정 */
    private fun setClickListener() {
        cl_drawer.setOnClickListener(this)
        ll_recommend_map.setOnClickListener(this)
        iv_gps.setOnClickListener(this)
        iv_menu.setOnClickListener(this)
        iv_drawer_close.setOnClickListener(this)
        iv_drawer_home.setOnClickListener(this)
        ll_bookmark_container.setOnClickListener(this)
        menu_culture.setOnClickListener(this)
        menu_festival.setOnClickListener(this)
        menu_food.setOnClickListener(this)
        menu_shopping.setOnClickListener(this)
        menu_sports.setOnClickListener(this)
        menu_tour.setOnClickListener(this)
        menu_travel.setOnClickListener(this)

        menu_food.isSelected = true
    }

    private fun convertStringFrom(id: Int): TourType =
        when (id) {
            R.id.menu_food -> TourType.FOOD
            R.id.menu_culture -> TourType.CULTURE
            R.id.menu_festival -> TourType.FESTIVAL
            R.id.menu_shopping -> TourType.SHOPPING
            R.id.menu_sports -> TourType.REPORTS
            R.id.menu_travel -> TourType.TRAVEL
            R.id.menu_tour -> TourType.TOUR
            else -> TourType.FOOD
        }

}