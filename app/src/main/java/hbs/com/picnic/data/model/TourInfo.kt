package hbs.com.picnic.data.model

class TourInfo(
    var title: String,
    var datas: ArrayList<TourItemInfo>?
) {
    constructor() : this("", null)

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
    ) {
        constructor() : this(0, 0, 0.0, 0.0, "","","", "", "")
    }
}
