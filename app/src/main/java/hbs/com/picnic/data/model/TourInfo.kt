package hbs.com.picnic.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TourInfo(
    var title: String,
    var datas: ArrayList<TourItemInfo>?
) : Parcelable {
    constructor() : this("", null)

    @Parcelize
    data class TourItemInfo(
        var contentid: Int,
        var contenttypeid: Int,
        var mapx: Double,
        var mapy: Double,
        var cat2: String,
        var title: String,
        var tel: String,
        var firstimage:String,
        var firstimage2:String
    ) : Parcelable {
        constructor() : this(0, 0, 0.0, 0.0, "","","", "", "")
    }
}
