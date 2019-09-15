package hbs.com.picnic.content

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import hbs.com.picnic.R
import hbs.com.picnic.content.presenter.ContentPresenter
import hbs.com.picnic.utils.AuthManager
import kotlinx.android.synthetic.main.activity_content.*
import java.io.InputStream
import java.lang.Exception

class ContentActivity : AppCompatActivity(), ContentContract.View {
    private val contentPresenter by lazy {
        ContentPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        contentPresenter
            .getStaticMap(
                width = "300",
                height = "180",
                marker = "type:d|size:tiny|pos:127.1054221%2037.3591614"
            )

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