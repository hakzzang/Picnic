package hbs.com.picnic.recommend.presenter

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hbs.com.picnic.recommend.RecommendContract
import hbs.com.picnic.recommend.usecase.RecommendUseCaseImpl
import hbs.com.picnic.utils.BaseContract
import android.location.LocationManager
import android.content.Intent
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import org.json.JSONObject

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
                    orders = "roadaddr",
                    output = "json"
                )
            }
        }
    }

    override fun getTourInfo() {
        view.updateBottoms()
    }
}