package hbs.com.picnic.remote

import io.reactivex.Observable
import okhttp3.ResponseBody

interface MapRepository {
    fun getStaticMap(
        width: String,
        height: String,
        marker: String
    ): Observable<ResponseBody>
}

class MapRepositoryImpl(private val mapAPI: MapAPI) : MapRepository {
    override fun getStaticMap(
        width: String,
        height: String,
        marker: String
    ): Observable<ResponseBody> {
        return mapAPI.getJobCafeList(width, height, marker)
    }
}