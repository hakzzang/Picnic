package hbs.com.picnic.data.remote

import hbs.com.picnic.data.model.TourDetail
import hbs.com.picnic.data.model.TourDetailRequest
import hbs.com.picnic.data.model.TourRequest
import io.reactivex.Observable
import okhttp3.ResponseBody


interface TourRepository {

    fun getListBasedLocation(
        request: TourRequest
    ): Observable<ResponseBody>

    fun getCommonDetail(
        request: TourRequest
    ): Observable<ResponseBody>

    fun getImageDetail(
        request: TourRequest
    ): Observable<ResponseBody>

    fun getDetailInfo(
        tourDetailRequest: TourDetailRequest
    ): Observable<ResponseBody>
}

class TourRepositoryImpl(private val tourAPI: TourAPI) : TourRepository {
    override fun getListBasedLocation(request: TourRequest): Observable<ResponseBody> {
        return tourAPI.getListBasedLocation(
            key = request.key,
            rows = request.rows,
            no = request.no,
            os = request.os,
            title = request.title,
            arrange = request.arrange,
            contentTypeId = request.contentTypeId,
            x = request.longitude,
            y = request.latitude,
            radius = request.radius
        )
    }

    override fun getCommonDetail(request: TourRequest): Observable<ResponseBody> {
        return tourAPI.getCommonDetail(
            key = request.key,
            rows = request.rows,
            no = request.no,
            title = request.title,
            contentId = request.contentId
        )
    }

    override fun getImageDetail(request: TourRequest): Observable<ResponseBody> {
        return tourAPI.getImageDetail(
            key = request.key,
            rows = request.rows,
            no = request.no,
            os = request.os,
            title = request.title,
            contentId = request.contentId,
            imageYN = request.imageYN
        )
    }

    override fun getDetailInfo(tourDetailRequest: TourDetailRequest): Observable<ResponseBody> {
        return tourAPI.getDetailInfo(
            TourAPI.API.ID,
            tourDetailRequest.numOfRows,
            tourDetailRequest.pageNo,
            tourDetailRequest.mobileOS,
            tourDetailRequest.mobileApp,
            tourDetailRequest.contentId,
            tourDetailRequest.contentTypeId
        )
    }
}