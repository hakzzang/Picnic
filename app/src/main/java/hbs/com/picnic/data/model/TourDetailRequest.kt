package hbs.com.picnic.data.model

import com.google.gson.annotations.SerializedName

data class TourDetailRequest(
    @SerializedName("serviceKey") val serviceKey: String,
    @SerializedName("numOfRows") val numOfRows: Int,
    @SerializedName("pageNo") val pageNo: Int,
    @SerializedName("MobileOS") val mobileOS: String,
    @SerializedName("MobileApp") val mobileApp: String,
    @SerializedName("contentId") val contentId: Int,
    @SerializedName("contentTypeId") val contentTypeId: Int
)