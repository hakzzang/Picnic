package hbs.com.picnic.ui.recommend.usecase

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import hbs.com.picnic.data.model.TourRequest
import hbs.com.picnic.data.remote.MapRepositoryImpl
import hbs.com.picnic.data.remote.RetrofitProvider
import hbs.com.picnic.data.remote.TourRepositoryImpl
import hbs.com.picnic.utils.BaseUrl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

interface RecommendUseCase {
    fun getLocationInfo(
        coords: String,
        orders: String,
        output: String
    ): Observable<ResponseBody>

    fun getLastLocation(): Location?

    fun getTourInfoBasedLocation(
        requests: List<TourRequest>
    ): List<Observable<ResponseBody>>
}

class RecommendUseCaseImpl(private val locationManager: LocationManager) : RecommendUseCase {
    private val mapRepository = MapRepositoryImpl(RetrofitProvider.provideMapApi(BaseUrl.MAP.url))
    private val tourRepository = TourRepositoryImpl(RetrofitProvider.provideTourApi(BaseUrl.TOUR_KOREA.url))

    override fun getLocationInfo(coords: String, orders: String, output: String): Observable<ResponseBody> {
        return mapRepository.getLocationInfo(coords, orders, output)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    @SuppressLint("MissingPermission")
    override fun getLastLocation(): Location? {
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

    }

    override fun getTourInfoBasedLocation(requests: List<TourRequest>): List<Observable<ResponseBody>> {
        var list:ArrayList<Observable<ResponseBody>> = arrayListOf()
        for(request in requests){
            list.add(tourRepository
                .getListBasedLocation(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()))
        }
        return list
    }
}