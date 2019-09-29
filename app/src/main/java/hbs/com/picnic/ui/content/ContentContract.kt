package hbs.com.picnic.ui.content

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import hbs.com.picnic.data.model.TourDetail
import hbs.com.picnic.data.model.TourDetailRequest
import hbs.com.picnic.data.model.TourRequest

interface ContentContract {
    interface View {
        fun setMapImage(mapImage: ByteArray)
        fun addSendListener(firebaseUser: FirebaseUser?)
        fun updateDetailInfo(tourDetail: TourDetail)
    }

    interface Presenter {
        fun getTourDetail(tourDetailRequest: TourDetailRequest)
        fun getStaticMap(width: String, height: String, marker: String)
        fun getAuth(context: Context)
    }
}