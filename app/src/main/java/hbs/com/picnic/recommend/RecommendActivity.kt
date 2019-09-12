package hbs.com.picnic.recommend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import hbs.com.picnic.R
import hbs.com.picnic.data.model.RecommendBottom
import hbs.com.picnic.utils.CustomItemDecoration
import hbs.com.picnic.view.recommend.RecommendBottomAdapter
import kotlinx.android.synthetic.main.activity_recommend.*

class RecommendActivity : AppCompatActivity() {
    private val itemSpace:Int by lazy { resources.getDimension(R.dimen.recommend_bottom_space).toInt() }
    private val itemDecoration: CustomItemDecoration by lazy {
        CustomItemDecoration(RecyclerView.VERTICAL, itemSpace)
    }

    private var bottomDatas: ArrayList<RecommendBottom> =
        arrayListOf(
            RecommendBottom(
                "선선한 가을에는 고궁 산책", arrayListOf(
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
                "아이들의 간식은 소중히", arrayListOf(
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


    private val bottomAdapter: RecommendBottomAdapter by lazy {
        RecommendBottomAdapter(this@RecommendActivity, bottomDatas)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        rv_recommend_bottom.apply {
            layoutManager = LinearLayoutManager(this@RecommendActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(itemDecoration)
            adapter = bottomAdapter
        }
    }

    private fun update(datas: ArrayList<RecommendBottom>) {
        bottomDatas = datas
        bottomAdapter.notifyDataSetChanged()
    }
}