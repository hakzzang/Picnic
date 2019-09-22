package hbs.com.picnic.ui.recommend

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_recommend.*
import kotlin.collections.ArrayList
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import hbs.com.picnic.data.model.TourInfo
import hbs.com.picnic.data.model.TourRequest
import hbs.com.picnic.data.remote.TourAPI
import hbs.com.picnic.utils.TourType
import hbs.com.picnic.utils.XmlParser
import org.json.JSONObject


class RecommendActivity : AppCompatActivity(), RecommendContract.View, View.OnClickListener {
    val REQUEST_SELECT_CODE: Int = 1002;

    private val locationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private val recommendPresenter by lazy {
        RecommendPresenter(this, locationManager)
    }

    private val bottomAdapter: RecommendBottomAdapter by lazy {
        RecommendBottomAdapter(this@RecommendActivity, bottomDatas, currentLocation)
    }

    private val itemDecoration: CustomItemDecoration by lazy {
        CustomItemDecoration(RecyclerView.VERTICAL, resources.getDimension(R.dimen.recommend_bottom_space).toInt())
    }

    private var bottomDatas: ArrayList<TourInfo> = arrayListOf()

    private var currentLong: Double = 0.0
    private var currentLat: Double = 0.0
    private var currentLocation: Location = Location("current")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        ll_recommend_map.setOnClickListener(this)
        iv_gps.setOnClickListener(this)

        rv_recommend_bottom.apply {
            layoutManager = LinearLayoutManager(this@RecommendActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(itemDecoration as RecyclerView.ItemDecoration)
            adapter = bottomAdapter
        }

        recommendPresenter
            .getGpsInfo(this@RecommendActivity)

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
        }
    }


    override fun updateLocation(name: String) {
        tv_recommend_map.text = getLocationFrom(name)

        // TODO : Food 뿐만 아니라 다른 타입의 데이터를 랜덤으로 가져와야 함.
        recommendPresenter.getTourInfo(
            arrayListOf(
                TourRequest(
                    TourAPI.API.ID,
                    10,
                    1,
                    TourAPI.API.OS,
                    "Picnic",
                    "B",
                    TourType.FOOD.value,
                    0,
                    currentLong,
                    currentLat,
                    5000
                )
            )
        )
    }

    override fun updateTourInfo(datas: String) {
        bottomAdapter.notifyDatas(arrayListOf(XmlParser.tourParser(datas)), currentLocation)
    }

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
        when (requestCode) {
            recommendPresenter.REQUEST_PERMISSION_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    recommendPresenter.getGpsInfo(this@RecommendActivity)
                } else {
                    Toast.makeText(this, resources.getString(R.string.permission_message), Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun makeRandomList():List<TourRequest>{
        return listOf(

        )
    }
    private fun getLocationFrom(jsonString: String): String {
        var resultString = "위치 정보를 불러오지 못했습니다."
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
}