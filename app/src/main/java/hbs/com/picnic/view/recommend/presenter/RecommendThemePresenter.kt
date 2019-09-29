package hbs.com.picnic.view.recommend.presenter

import android.content.res.AssetManager
import android.location.Location
import android.util.Log
import hbs.com.picnic.data.model.ParkInfo
import hbs.com.picnic.utils.BaseContract
import hbs.com.picnic.utils.MenuType
import hbs.com.picnic.view.recommend.RecommendThemeContract
import hbs.com.picnic.view.recommend.usecase.RecommendMenuUseCaseImpl

class RecommendThemePresenter(
    private val view: RecommendThemeContract.View,
    assets: AssetManager
) : RecommendThemeContract.Presenter, BaseContract.Presenter() {

    private val menuUseCase = RecommendMenuUseCaseImpl(assets)

    override fun clickTheme(lat:Double, lon:Double){
        view.nearByThemes(lat, lon)
    }

    override fun readFile(currentLocation: Location) {
        val datas = menuUseCase.readCSVFile("seoul_park.csv")

        for(park in datas){
            val themeLocation = Location("theme")
            themeLocation.apply {
                longitude = park.x1.toDouble()
                latitude = park.y1.toDouble()
            }
            park.distance = getDistance(themeLocation, currentLocation)
        }

        view.notifyDatas(datas)
    }


    private fun getDistance(target: Location, currentLocation: Location): Float =
        target.distanceTo(currentLocation) / 1000

}