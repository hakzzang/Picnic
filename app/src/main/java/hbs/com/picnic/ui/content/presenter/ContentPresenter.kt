package hbs.com.picnic.ui.content.presenter

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import hbs.com.picnic.ui.content.ContentContract
import hbs.com.picnic.ui.content.usecase.ContentUseCaseImpl
import hbs.com.picnic.utils.AuthManager
import hbs.com.picnic.utils.BaseContract
import java.lang.Exception

class ContentPresenter(private val view: ContentContract.View) : BaseContract.Presenter(), ContentContract.Presenter {
    private val contentUseCase = ContentUseCaseImpl()

    override fun getStaticMap(width: String, height: String, marker: String) {
        contentUseCase.getStaticMap(width, height, marker).subscribe({ response ->
            val mapImage = response.bytes()
            view.setMapImage(mapImage)
        }, { error -> Log.d("error", error.message) }).let { addDisposable(it) }
    }

    override fun getAuth(context: Context) {
        addDisposable(AuthManager.login(context, object : AuthManager.OnLoginAnonymousAuth {
            override fun onCompleted(firebaseUser: FirebaseUser?) {
                view.addSendListener(firebaseUser)
            }

            override fun onFail(exception: Exception?) {

            }
        }))
    }

}