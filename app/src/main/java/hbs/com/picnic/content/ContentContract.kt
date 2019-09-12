package hbs.com.picnic.content

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.io.InputStream

interface ContentContract {
    interface View {
        fun setMapImage(mapImage: ByteArray)
    }

    interface Presenter {
        fun getStaticMap(width: String, height: String, marker: String)
    }
}