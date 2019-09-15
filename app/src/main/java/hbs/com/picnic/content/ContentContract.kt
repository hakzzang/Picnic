package hbs.com.picnic.content

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.io.InputStream

interface ContentContract {
    interface View {
        fun setMapImage(mapImage: ByteArray)
        fun addSendListener(firebaseUser: FirebaseUser?)
    }

    interface Presenter {
        fun getStaticMap(width: String, height: String, marker: String)
        fun getAuth(context: Context)
    }
}