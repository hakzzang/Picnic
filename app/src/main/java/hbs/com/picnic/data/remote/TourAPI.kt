package hbs.com.picnic.data.remote

import hbs.com.picnic.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Query

interface TourAPI {
    object API {
        const val ID = BuildConfig.TOUR_API_KEY
        const val OS = "AND"
    }


    /**위치기반 관광정보**/
    @Headers("Content-Type: application/json")
    @GET("locationBasedList")
    fun getListBasedLocation(
        @Query(value = "ServiceKey", encoded = true) key: String,
        @Query("numOfRows") rows: Int,
        @Query("pageNo") no: Int,
        @Query("MobileOS") os: String,
        @Query("MobileApp") title: String,
        @Query("arrange") arrange: String,
        @Query("contentTypeId") contentTypeId: Int,
        @Query("mapX") x: Double,
        @Query("mapY") y: Double,
        @Query("radius") radius: Int
    ): Observable<ResponseBody>

    /** 공통 정보 조회 **/
    @Headers("Content-Type: application/json")
    @GET("detailCommon")
    fun getCommonDetail(
        @Query(value = "ServiceKey", encoded = true) key: String,
        @Query("numOfRows") rows: Int,
        @Query("pageNo") no: Int,
        @Query("MobileApp") title: String,
        @Query("contentId") contentId: Int
    ): Observable<ResponseBody>

    /** 이미지 정보 조회 **/
    @Headers("Content-Type: application/json")
    @GET("detailImage")
    fun getImageDetail(
        @Query(value = "ServiceKey", encoded = true) key: String,
        @Query("numOfRows") rows: Int,
        @Query("pageNo") no: Int,
        @Query("MobileOS") os: String,
        @Query("MobileApp") title: String,
        @Query("contentId") contentId: Int,
        @Query("imageYN") imageYN: String //Y=콘텐츠 이미지 조회, N='음식점'타입의 음식메뉴 이미지
    ): Observable<ResponseBody>


    /**소개 정보 조회**/
    @Headers("Content-Type: application/json")
    @GET("detailIntro")
    fun getDetailInfo(
        @Query(value = "ServiceKey", encoded = true) key: String,
        @Query("numOfRows") rows: Int,
        @Query("pageNo") no: Int,
        @Query("MobileOS") os: String,
        @Query("MobileApp") title: String,
        @Query("contentId") contentId: Int,
        @Query("contentTypeId") contentTypeId: Int
    ): Observable<ResponseBody>

}