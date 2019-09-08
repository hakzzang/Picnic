package hbs.com.picnic.remote

import hbs.com.picnic.BuildConfig
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MapAPI {
    object API {
        const val ID = "X-NCP-APIGW-API-KEY-ID: " + BuildConfig.CLIENT_ID
        const val KEY = "X-NCP-APIGW-API-KEY: " + BuildConfig.CLIENT_SECRET
    }

    @Headers("Content-Type: application/json", API.ID, API.KEY)
    @GET("/map-static/v2/raster")
    fun getJobCafeList(
        @Query("w") width: String,
        @Query("h") height: String,
        @Query(value = "markers", encoded = true) marker: String
    ): Observable<ResponseBody>
}