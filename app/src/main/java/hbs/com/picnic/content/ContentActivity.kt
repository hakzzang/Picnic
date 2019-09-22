package hbs.com.picnic.content

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import hbs.com.picnic.content.presenter.ContentPresenter
import kotlinx.android.synthetic.main.activity_content.*


class ContentActivity : AppCompatActivity(), ContentContract.View {
    private val contentPresenter by lazy {
        ContentPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(hbs.com.picnic.R.layout.activity_content)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}