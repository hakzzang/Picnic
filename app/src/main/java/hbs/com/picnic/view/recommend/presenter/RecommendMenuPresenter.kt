package hbs.com.picnic.view.recommend.presenter

import android.content.res.AssetManager
import android.util.Log
import hbs.com.picnic.utils.BaseContract
import hbs.com.picnic.utils.MenuType
import hbs.com.picnic.view.recommend.RecommendMenuContract
import hbs.com.picnic.view.recommend.usecase.RecommendMenuUseCaseImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RecommendMenuPresenter(
    private val view: RecommendMenuContract.View,
    assets: AssetManager
) : RecommendMenuContract.Presenter, BaseContract.Presenter() {

    private val menuUseCase = RecommendMenuUseCaseImpl(assets)

    override fun makeMenuList() {
        view.setMenuList(MenuType.values().toList().subList(0, 4))
    }

    override fun clickMenu(type: MenuType) {
        menuUseCase.readCSVFile(type.fileName)
        // TODO : view 로 데이터 전송 필요.
    }
}