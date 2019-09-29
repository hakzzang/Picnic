package hbs.com.picnic.view.recommend

import android.location.Location
import hbs.com.picnic.data.model.ParkInfo
import hbs.com.picnic.utils.MenuType

interface RecommendThemeContract {
    interface View{
        fun notifyDatas(themes:ArrayList<ParkInfo>)
        fun nearByThemes(lat:Double, lon:Double)
    }

    interface Presenter{
        fun clickTheme(lat:Double, lon:Double)
        fun readFile(currentLocation: Location)
    }
}