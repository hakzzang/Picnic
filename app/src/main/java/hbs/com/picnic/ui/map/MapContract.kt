package hbs.com.picnic.ui.map

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker

interface MapContract {
    interface View {
        fun singleMarker(marker:Marker)
        fun multipleMarker(markers:List<Marker>)
    }

    interface Presenter {
        fun selectMap(latLng: LatLng)
        fun fullMap(latLng: LatLng)
        fun listMap(latLng: LatLng)
    }
}