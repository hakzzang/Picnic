package hbs.com.picnic.ui.recommend.presenter

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hbs.com.picnic.ui.recommend.RecommendContract
import hbs.com.picnic.ui.recommend.usecase.RecommendUseCaseImpl
import hbs.com.picnic.utils.BaseContract
import android.location.LocationManager
import hbs.com.picnic.data.model.TourRequest
import hbs.com.picnic.data.remote.TourAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RecommendPresenter(private val view: RecommendContract.View, locationManager: LocationManager) :
    BaseContract.Presenter(), RecommendContract.Presenter {

    val REQUEST_PERMISSION_LOCATION: Int = 1001;

    private val recommendUseCase = RecommendUseCaseImpl(locationManager)

    override fun getLocationInfo(coords: String, orders: String, output: String) {
        recommendUseCase.getLocationInfo(coords, orders, output).subscribe({ response ->
            val location = response.string()
            view.updateLocation(location)
        }, { error ->
        }).run {
            addDisposable(this)
        }
    }

    override fun getGpsInfo(context: Activity) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                view.showGPSDialogAgain()
            } else {
                ActivityCompat.requestPermissions(
                    context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
            }
        } else {
            recommendUseCase.getLastLocation()?.let { location ->
                getLocationInfo(
                    coords = "${location.longitude},${location.latitude}",
                    orders = "addr",
                    output = "json"
                )
            }
        }
    }

    override fun updateTourInfo(requestInfo: List<Int>, currentLong: Double, currentLat: Double) {
        val requestList = arrayListOf<TourRequest>()

        for (type in requestInfo) {
            val item = TourRequest(
                TourAPI.API.ID,
                10,
                1,
                TourAPI.API.OS,
                "Picnic",
                "B",
                0,
                0,
                currentLong,
                currentLat,
                5000
            )
            item.contentTypeId = type
            requestList.add(item)
        }

        getTourInfo(requestList)
    }

    override fun getTourInfo(reqeustInfo: List<TourRequest>) {
        val tourInfos = arrayListOf<String>()

        recommendUseCase.getTourInfoBasedLocation(reqeustInfo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
            .subscribe(
                { response ->
                    val location = response.string()
                    tourInfos.add(location)
                }, { error ->
                }, {
                    view.updateTourInfo(tourInfos)
                }).run {
                addDisposable(this)
            }
    }

    override fun getSelectedMenu() {

    }

    override fun onPause() {
        onClear()
    }
}