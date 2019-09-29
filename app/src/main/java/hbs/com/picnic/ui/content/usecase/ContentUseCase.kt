package hbs.com.picnic.ui.content.usecase

import android.util.Log
import hbs.com.picnic.data.model.TourDetail
import hbs.com.picnic.data.model.TourDetailHeader
import hbs.com.picnic.data.model.TourDetailRequest
import hbs.com.picnic.data.model.TourDetailResponse
import hbs.com.picnic.data.remote.MapRepositoryImpl
import hbs.com.picnic.data.remote.RetrofitProvider
import hbs.com.picnic.data.remote.TourRepositoryImpl
import hbs.com.picnic.utils.BaseUrl
import hbs.com.picnic.utils.XmlParser
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

interface ContentUseCase {
    fun getStaticMap(width: String, height: String, marker: String): Observable<ResponseBody>
    fun getTourDetail(tourDetailRequest: TourDetailRequest): Observable<ResponseBody>
}

class ContentUseCaseImpl : ContentUseCase {
    private val mapRepository = MapRepositoryImpl(RetrofitProvider.provideMapApi(BaseUrl.MAP.url))
    private val tourRepository = TourRepositoryImpl(RetrofitProvider.provideTourApi(BaseUrl.TOUR_KOREA.url))

    override fun getStaticMap(width: String, height: String, marker: String): Observable<ResponseBody> {
        return mapRepository
            .getStaticMap(width, height, marker)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    override fun getTourDetail(tourDetailRequest: TourDetailRequest): Observable<ResponseBody> =
        tourRepository.getDetailInfo(tourDetailRequest).subscribeOn(Schedulers.io())


}