package hbs.com.picnic

import android.app.Application
import com.naver.maps.map.NaverMapSdk
import android.content.ContextWrapper
import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull
import io.realm.Realm
import io.realm.RealmConfiguration


class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naver_map_id))
        Realm.init(this);
        val config = RealmConfiguration.Builder().name("picnic.realm").build();
        Realm.setDefaultConfiguration(config);
    }
}