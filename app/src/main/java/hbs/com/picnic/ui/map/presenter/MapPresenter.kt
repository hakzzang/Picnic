package hbs.com.picnic.ui.map.presenter

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import hbs.com.picnic.ui.map.MapContract
import hbs.com.picnic.utils.BaseContract

class MapPresenter(private val view: MapContract.View) :
    BaseContract.Presenter(),
    MapContract.Presenter {


    override fun selectMap(latLng: LatLng) {
        Marker().apply {
            position = latLng
            isIconPerspectiveEnabled = true
            view.singleMarker(this)
        }
    }

    override fun fullMap(latLng: LatLng) {
        Marker().apply {
            position = latLng
            isIconPerspectiveEnabled = true
            view.singleMarker(this)
        }
    }

    override fun listMap(latLng: LatLng) {
    }

}