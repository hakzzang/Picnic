package hbs.com.picnic.ui.content.usecase

import hbs.com.picnic.data.remote.MapRepositoryImpl
import hbs.com.picnic.data.remote.RetrofitProvider
import hbs.com.picnic.utils.BaseUrl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

interface ContentUseCase {
    fun getStaticMap(width: String, height: String, marker: String): Observable<ResponseBody>
}

class ContentUseCaseImpl : ContentUseCase {
    private val mapRepository = MapRepositoryImpl(RetrofitProvider.provideMapApi(BaseUrl.MAP.url))
    override fun getStaticMap(width: String, height: String, marker: String): Observable<ResponseBody> {
        return mapRepository
            .getStaticMap(width, height, marker)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}