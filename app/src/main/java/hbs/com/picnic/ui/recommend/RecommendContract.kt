package hbs.com.picnic.ui.recommend

import android.app.Activity
import hbs.com.picnic.data.model.TourRequest

interface RecommendContract {
    interface View {
        fun showGPSDialogAgain()
        fun updateLocation(name:String)
        fun updateTourInfo(datas:List<String>)
    }

    interface Presenter {
        fun getGpsInfo(context: Activity)
        fun getLocationInfo(coords: String, orders: String, output: String)
        fun updateTourInfo(requestInfo: List<Int>, currentLong:Double, currentLat:Double )
        fun getTourInfo(reqeustInfo:List<TourRequest>)
        fun onPause()

        fun getSelectedMenu()
    }
}