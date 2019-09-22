package hbs.com.picnic.recommend

import android.app.Activity
import hbs.com.picnic.data.model.TourRequest

interface RecommendContract {
    interface View {
        fun showGPSDialogAgain()
        fun updateLocation(name:String)
        fun updateTourInfo(datas:String)
    }

    interface Presenter {
        fun getGpsInfo(context: Activity)
        fun getLocationInfo(coords: String, orders: String, output: String)
        fun getTourInfo(reqeustInfo:List<TourRequest>)
    }
}