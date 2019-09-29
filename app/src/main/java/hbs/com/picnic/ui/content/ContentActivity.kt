package hbs.com.picnic.ui.content

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import hbs.com.picnic.R
import hbs.com.picnic.data.model.TourInfo
import hbs.com.picnic.ui.content.presenter.ContentPresenter
import kotlinx.android.synthetic.main.activity_content.*
import android.util.DisplayMetrics
import hbs.com.picnic.data.model.TourDetail
import hbs.com.picnic.data.model.TourDetailRequest
import hbs.com.picnic.data.model.TourRequest
import hbs.com.picnic.data.remote.TourAPI

class ContentActivity : AppCompatActivity(), ContentContract.View {
    private val contentPresenter by lazy {
        ContentPresenter(this)
    }

    private val tourItemInfo by lazy {
        intent.getParcelableExtra<TourInfo.TourItemInfo>("tourItemInfo")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        content_view.initTourItemInfo(tourItemInfo)
        content_view.initView(tourItemInfo)
        content_view.getChatContents(tourItemInfo)
        contentPresenter
            .getStaticMap(
                width = "375",
                height = "258",
                marker = "type:t|size:tiny|pos:${String.format("%.7f", tourItemInfo.mapx.toFloat())}%20${
                String.format("%.7f", tourItemInfo.mapy)}|label:${tourItemInfo.title}"
            )
        contentPresenter.getAuth(this)
        contentPresenter.getTourDetail(
            TourDetailRequest(
                TourAPI.API.ID,
                10,
                1,
                TourAPI.API.OS,
                "PICNIC",
                tourItemInfo.contentid,
                tourItemInfo.contenttypeid
            )
        )
    }

    override fun onPause() {
        super.onPause()
        contentPresenter.onClear()
    }

    override fun setMapImage(mapImage: ByteArray) {
        content_view.updateMap(mapImage)
    }

    override fun addSendListener(firebaseUser: FirebaseUser?) {
        content_view.addSendListener(firebaseUser)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun updateDetailInfo(tourDetail: TourDetail) {
        content_view.updateDetailInfo(tourDetail)
    }
}