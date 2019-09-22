package hbs.com.picnic.data.remote

import io.reactivex.Observable
import okhttp3.ResponseBody

interface MapRepository {
    fun getStaticMap(
        width: String,
        height: String,
        marker: String
    ): Observable<ResponseBody>

    fun getLocationInfo(
        coords:String,
        orders:String,
        output:String
    ): Observable<ResponseBody>
}

class MapRepositoryImpl(private val mapAPI: MapAPI) : MapRepository {
    override fun getLocationInfo(coords: String, orders: String, output: String): Observable<ResponseBody> {
        return mapAPI.getLocationInfo(
            coords, orders, output
        )
    }


    override fun getStaticMap(
        width: String,
        height: String,
        marker: String
    ): Observable<ResponseBody> {
        return mapAPI.getStaticMap(width, height, marker)
    }
}