package hbs.com.picnic.data.model

data class RecommendBottom(
    val title: String,
    val datas: ArrayList<BottomInfo>
) {
    data class BottomInfo(
        val id:Int,
        val title: String,
        val image: String,
        val tags:String
    )
}
