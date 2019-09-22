package hbs.com.picnic.ui.content

import android.content.Context
import com.google.firebase.auth.FirebaseUser

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