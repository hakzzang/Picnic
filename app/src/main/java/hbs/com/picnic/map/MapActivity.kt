package hbs.com.picnic.map

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.activity_map.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    private val latitude by lazy {
        intent.getDoubleExtra("latitude", 0.0)
    }
    private val longitude by lazy {
        intent.getDoubleExtra("longitude", 0.0)
    }

    private val placeCaption = "테스트 캡션"

    private lateinit var fusedLocationSource: FusedLocationSource
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(hbs.com.picnic.R.layout.activity_map)
        fusedLocationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        map_view.getMapAsync(this)
    }

    override fun onMapReady(mapInstance: NaverMap) {
        val latLng = LatLng(latitude, longitude)
        val cameraUpdate = CameraUpdate.scrollTo(latLng)
            .animate(CameraAnimation.Easing)

        Marker().apply {
            position = latLng
            isIconPerspectiveEnabled = true
            captionText = placeCaption
            map = mapInstance
        }

        mapInstance.apply {
            isIndoorEnabled = true
            symbolScale = 2f
            locationSource = fusedLocationSource
        }
        mapInstance.uiSettings.apply {
            isIndoorLevelPickerEnabled = true
            isZoomControlEnabled = true
            isCompassEnabled = true
            isLogoClickEnabled = true
            isLocationButtonEnabled = true
        }

        mapInstance.moveCamera(cameraUpdate)
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