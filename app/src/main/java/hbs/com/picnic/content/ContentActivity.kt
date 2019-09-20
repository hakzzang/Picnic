package hbs.com.picnic.content

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import hbs.com.picnic.R
import hbs.com.picnic.content.presenter.ContentPresenter
import hbs.com.picnic.data.model.CloudMessage
import hbs.com.picnic.data.model.Notification
import hbs.com.picnic.remote.FcmRepositoryImpl
import hbs.com.picnic.remote.RetrofitProvider
import hbs.com.picnic.utils.BaseUrl
import hbs.com.picnic.utils.KakaoManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : AppCompatActivity(), ContentContract.View {
    private val contentPresenter by lazy {
        ContentPresenter(this)
    }

    private val kakaoManager by lazy { KakaoManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        contentPresenter
            .getStaticMap(
                width = "300",
                height = "180",
                marker = "type:d|size:tiny|pos:127.1054221%2037.3591614"
            )

        kakaoManager.sendMsg("hakzznag","https://t1.daumcdn.net/cfile/tistory/277DA93B586C7C180F")
        val fcmFcmRepository =
            FcmRepositoryImpl(RetrofitProvider.provideFcmApi(BaseUrl.FIREBASE.url))

        /*FirebaseMessaging.getInstance().subscribeToTopic("hello")

        fcmFcmRepository.sendMessage("hello", CloudMessage("hello1", Notification("h","j")))
            .subscribe ({
                Log.d("reponse",it.toString())
            },{
                Log.d("reponsed",it.toString())
            })*/
        contentPresenter.getAuth(this)
    }

    override fun onPause() {
        super.onPause()
        contentPresenter.onClear()
    }

    override fun setMapImage(mapImage: ByteArray) {
        content_view.updateMap(mapImage)
    }

    override fun addSendListener(firebaseUser: FirebaseUser?) {
        content_view.addSendListener(firebaseUser)
    }
}