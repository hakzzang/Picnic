package hbs.com.picnic.recommend.usecase

import hbs.com.picnic.remote.MapRepositoryImpl
import hbs.com.picnic.remote.RetrofitProvider
import hbs.com.picnic.utils.BaseUrl
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

interface RecommendUseCase {
    fun getLocationInfo(
        coords:String,
        orders:String,
        output:String
    ):Observable<ResponseBody>
}

class RecommendUseCaseImpl : RecommendUseCase{
    private val mapRepository = MapRepositoryImpl(RetrofitProvider.provideMapApi(BaseUrl.MAP.url))

    override fun getLocationInfo(coords: String, orders: String, output: String): Observable<ResponseBody> {
        return mapRepository.getLocationInfo(coords, orders, output)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}