package hbs.com.picnic.recommend

import android.app.Activity

interface RecommendContract {
    interface View {
        fun setLocation(name:String)
        fun updateDatas()
    }

    interface Presenter {
        fun getGpsInfo(context: Activity)
        fun getLocationInfo(coords: String, orders: String, output: String)
        fun getTourInfo()
    }
}