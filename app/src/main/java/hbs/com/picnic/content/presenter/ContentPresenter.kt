package hbs.com.picnic.content.presenter

import android.util.Log
import hbs.com.picnic.content.ContentContract
import hbs.com.picnic.content.usecase.ContentUseCaseImpl
import hbs.com.picnic.utils.BaseContract

class ContentPresenter(private val view: ContentContract.View) : BaseContract.Presenter(), ContentContract.Presenter {
    private val contentUseCase = ContentUseCaseImpl()

    override fun getStaticMap(width: String, height: String, marker: String) {
        contentUseCase.getStaticMap(width, height, marker).subscribe({ response ->
            val mapImage = response.bytes()
            view.setMapImage(mapImage)
        }, { error -> Log.d("error", error.message) }).let { addDisposable(it) }
    }
}