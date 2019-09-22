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
import android.util.Log
import hbs.com.picnic.data.model.TourRequest

class RecommendPresenter(private val view: RecommendContract.View, locationManager: LocationManager) :
    BaseContract.Presenter(),
    RecommendContract.Presenter {

    val REQUEST_PERMISSION_LOCATION: Int = 1001;
    private val recommendUseCase = RecommendUseCaseImpl(locationManager)

    override fun getLocationInfo(coords: String, orders: String, output: String) {
        recommendUseCase.getLocationInfo(coords, orders, output).subscribe({ response ->
            val location = response.string()
            Log.d("getLocationInfo", location)
            view.updateLocation(location)
        }, { error ->
        }).let {
            addDisposable(it)
        }
    }

    override fun getGpsInfo(context: Activity) {
        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

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

    override fun getTourInfo(reqeustInfo: List<TourRequest>) {
        recommendUseCase.getTourInfoBasedLocation(reqeustInfo)[0]
            .subscribe(
                { response ->
                    val location = response.string()
                    view.updateTourInfo(location)
                }, { error ->
                }).let {
                addDisposable(it)
            }
    }
}