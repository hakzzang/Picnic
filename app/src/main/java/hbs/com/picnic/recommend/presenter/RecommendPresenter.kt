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
import org.json.JSONObject


class RecommendPresenter(private val view: RecommendContract.View) : BaseContract.Presenter(),
    RecommendContract.Presenter {
    val REQUEST_PERMISSION_LOCATION: Int = 1001;
    private val recommendUseCase = RecommendUseCaseImpl()

    override fun getLocationInfo(coords: String, orders: String, output: String) {
        recommendUseCase.getLocationInfo(coords, orders, output).subscribe({ response ->
            val location = response.string()
            view.setLocation(location)
        }, { error ->
        }).let {
            addDisposable(it)
        }
    }

    override fun getGpsInfo(context: Activity) {
        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,Manifest.permission.ACCESS_FINE_LOCATION)){
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    .apply {
                        setTitle("피크닉")
                        setMessage("GPS 권한을 위해서는 권한 허용 필요")
                            .setPositiveButton("설정으로 이동") { _, _ ->
                                Intent(ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.packageName))
                                    .apply {
                                        addCategory(Intent.CATEGORY_DEFAULT)
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        context.startActivity(this)
                                    }
                            }
                            .setNegativeButton("취소") { dialog, _ -> dialog?.dismiss() }
                    }
                builder.show()
            } else {
                ActivityCompat.requestPermissions(
                    context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
            }
        } else {
            val lm: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?.let {
                    getLocationInfo(
                        coords = "${it.longitude},${it.latitude}",
                        orders = "roadaddr",
                        output = "json"
                    )
                }

        }
    }

    override fun getTourInfo() {
        view.updateDatas()
    }
}