package hbs.com.picnic.ui.map

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import hbs.com.picnic.ui.map.presenter.MapPresenter
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(),
    MapContract.View,
    OnMapReadyCallback {

    enum class Type(val value: Int) {
        FULL_MAP(0), SELECT_MAP(1), LIST_MAP(2)
    }

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    private val type by lazy {
        intent.getIntExtra("type", Type.FULL_MAP.value)
    }

    private val mapPresenter by lazy {
        MapPresenter(this)
    }

    private var latitude:Double = 0.0
    private var longitude:Double = 0.0

    private lateinit var naverMap: NaverMap
    private lateinit var cameraUpdate: CameraUpdate

    private lateinit var fusedLocationSource: FusedLocationSource
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(hbs.com.picnic.R.layout.activity_map)

        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)

        fusedLocationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        map_view.getMapAsync(this)

        btn_select.setOnClickListener {
            Log.d("MapActivity", latitude.toString())
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onMapReady(mapInstance: NaverMap) {
        naverMap = mapInstance

        when (type) {
            Type.SELECT_MAP.value or Type.FULL_MAP.value -> {
                initMap()

                naverMap.apply {
                    isIndoorEnabled = true
                }
                naverMap.uiSettings.apply {
                    isIndoorLevelPickerEnabled = true
                    isZoomControlEnabled = true
                    isCompassEnabled = true
                    isLogoClickEnabled = true
                    isLocationButtonEnabled = true
                }

                if (type == Type.SELECT_MAP.value) {
                    tv_select.visibility = VISIBLE
                    naverMap.setOnMapClickListener { _, latLng ->
                        latitude = latLng.latitude
                        longitude = latLng.longitude

                        cameraUpdate = CameraUpdate.scrollTo(latLng)
                            .animate(CameraAnimation.Easing)
                        mapPresenter.selectMap(latLng)
                        tv_select.visibility = GONE
                        btn_select.visibility = VISIBLE
                    }
                }
            }
            Type.LIST_MAP.value -> {

            }
            else -> {
                initMap()
            }
        }

    }

    private fun initMap() {
        val latLng = LatLng(latitude, longitude)

        cameraUpdate = CameraUpdate.scrollTo(latLng)
            .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
        mapPresenter.fullMap(latLng)
    }

    override fun singleMarker(marker: Marker) {
        marker.map = naverMap
        naverMap.moveCamera(cameraUpdate)
    }

    override fun multipleMarker(markers: List<Marker>) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>, @NonNull grantResults: IntArray
    ) {

        if (fusedLocationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults
            )
        ) {
            return
        }
        super.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        )
    }

    override fun onStart() {
        super.onStart()
        map_view.onStart()
    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map_view.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        map_view.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view.onLowMemory()
    }
}