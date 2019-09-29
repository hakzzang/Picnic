package hbs.com.picnic.data.model

data class TourRequest (
    val key:String,
    val rows:Int,
    val no:Int,
    val os:String,
    val title:String,
    val arrange:String,
    val contentTypeId:Int,
    val contentId:Int,
    val longitude:Double,
    val latitude:Double,
    val radius:Int,
    val imageYN:String="Y"
)