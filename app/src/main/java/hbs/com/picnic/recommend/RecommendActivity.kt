package hbs.com.picnic.recommend

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
import hbs.com.picnic.data.model.RecommendBottom
import hbs.com.picnic.map.MapActivity
import hbs.com.picnic.recommend.presenter.RecommendPresenter
import hbs.com.picnic.utils.CustomItemDecoration
import hbs.com.picnic.view.recommend.adapter.RecommendBottomAdapter
import kotlinx.android.synthetic.main.activity_recommend.*
import kotlin.collections.ArrayList
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
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
        RecommendBottomAdapter(this@RecommendActivity, bottomDatas)
    }

    private val itemDecoration: CustomItemDecoration by lazy {
        CustomItemDecoration(RecyclerView.VERTICAL, resources.getDimension(R.dimen.recommend_bottom_space).toInt())
    }

    private var bottomDatas: ArrayList<RecommendBottom> =
        arrayListOf(
            RecommendBottom(
                "금강산도 식후경! 맛있는 피크닉", arrayListOf(
                    RecommendBottom.BottomInfo(
                        0,
                        "새우버거의 치즈는 누가 훔쳤나",
                        "https://t1.daumcdn.net/cfile/tistory/275CDB3B586C7C172C",
                        "#새우버거 #치즈 #화남"
                    ),
                    RecommendBottom.BottomInfo(
                        1,
                        "홍주의 햄버거는 누가 훔쳤나",
                        "https://t1.daumcdn.net/cfile/tistory/275CDB3B586C7C172C",
                        "#홍주 #햄버거 #화남"
                    ),
                    RecommendBottom.BottomInfo(
                        2,
                        "감자튀김의 젤리는 누가 훔쳤나",
                        "https://t1.daumcdn.net/cfile/tistory/277DA93B586C7C180F",
                        "#감자튀김 #젤리 #화남"
                    )
                )
            ),
            RecommendBottom(
                "지갑이 열리네요~ 지름신이 들어오죠~", arrayListOf(
                    RecommendBottom.BottomInfo(
                        0,
                        "새우버거의 초코는 누가 훔쳤나",
                        "https://t1.daumcdn.net/cfile/tistory/2779DE3B586C7C1813",
                        "#새우버거 #초코 #화남"
                    ),
                    RecommendBottom.BottomInfo(
                        1,
                        "감자튀김의 사탕은 누가 훔쳤나",
                        "https://t1.daumcdn.net/cfile/tistory/2668B93B586C7C1823",
                        "#감자튀김 #사탕 #화남"
                    )
                )
            ),
            RecommendBottom(
                "교양있는 피크닉", arrayListOf(
                    RecommendBottom.BottomInfo(
                        0,
                        "새우버거의 초코는 누가 훔쳤나",
                        "https://t1.daumcdn.net/cfile/tistory/2779DE3B586C7C1813",
                        "#새우버거 #초코 #화남"
                    ),
                    RecommendBottom.BottomInfo(
                        1,
                        "감자튀김의 사탕은 누가 훔쳤나",
                        "https://t1.daumcdn.net/cfile/tistory/2668B93B586C7C1823",
                        "#감자튀김 #사탕 #화남"
                    )
                )
            )
        )

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0


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
                        putExtra("longitude", longitude)
                        putExtra("latitude", latitude)
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
        Toast.makeText(this@RecommendActivity, resources.getString(R.string.update_location), Toast.LENGTH_SHORT).show()
    }

    override fun updateBottoms() {

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
                    orders = "roadaddr",
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

    private fun getLocationFrom(jsonString: String): String {
        var resultString = "위치 정보를 불러오지 못했습니다."
        val jsonObject = JSONObject(jsonString)
        with(jsonObject) {
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

                longitude = coordsObj.getDouble("x")
                latitude = coordsObj.getDouble("y")
            }
        }
        return resultString
    }
}