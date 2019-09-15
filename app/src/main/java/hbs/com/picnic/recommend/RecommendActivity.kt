package hbs.com.picnic.recommend

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
import org.json.JSONObject


class RecommendActivity : AppCompatActivity(), RecommendContract.View, View.OnClickListener {
    private val recommendPresenter by lazy {
        RecommendPresenter(this)
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        rv_recommend_bottom.apply {
            layoutManager = LinearLayoutManager(this@RecommendActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(itemDecoration as RecyclerView.ItemDecoration)
            adapter = bottomAdapter
        }

        ll_recommend_map.setOnClickListener(this)
        iv_gps.setOnClickListener(this)

        recommendPresenter
            .getLocationInfo(
                coords = "126.98955,37.5651",
                orders = "roadaddr",
                output = "json"
            )

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_recommend_map -> {
                Intent(this@RecommendActivity, MapActivity::class.java)
                    .apply {
                        putExtra("longitude", 126.98955)
                        putExtra("latitude", 37.5651)
                        startActivity(this)
                    }
            }

            R.id.iv_gps -> {
                recommendPresenter.getGpsInfo(this@RecommendActivity)
            }
        }
    }


    override fun setLocation(name: String) {
        tv_recommend_map.text = getLocationFrom(name)
        Toast.makeText(this, "위치 정보가 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun updateDatas() {
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            recommendPresenter.REQUEST_PERMISSION_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    recommendPresenter.getGpsInfo(this@RecommendActivity)
                } else {
                    Toast.makeText(this, "아직 승인받지 않았습니다.", Toast.LENGTH_LONG).show()
                }

            }
        }
    }


    private fun getLocationFrom(jsonString: String):String{
        var resultString = ""
        val jsonObject = JSONObject(jsonString)
        with(jsonObject) {
            val regionObject = getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("region")

            with(regionObject){
                resultString =
                    "${getJSONObject("area1").getString("name")} " +
                            "${getJSONObject("area2").getString("name")} " +
                            "${getJSONObject("area3").getString("name")}"
            }
        }
        return resultString
    }
}