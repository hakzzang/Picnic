package hbs.com.picnic.data.model


data class LocationInfo(
    val code:Int,
    val status:Status,
    val results:List<LocationResults>
){
    data class Status(
        val code:Int
    )

    data class LocationResults(
        val name:String,
        val region:List<String>
    )
}