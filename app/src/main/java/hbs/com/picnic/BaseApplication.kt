package hbs.com.picnic

import android.app.Application
import com.naver.maps.map.NaverMapSdk

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naver_map_id))
    }
}