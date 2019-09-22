package hbs.com.picnic.view.content

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseUser
import hbs.com.picnic.R
import hbs.com.picnic.content.adapter.ChattingAdapter
import hbs.com.picnic.data.model.ChatMessage
import hbs.com.picnic.data.model.CloudMessage
import hbs.com.picnic.databinding.ViewContentBinding
import hbs.com.picnic.utils.AnimationUtils
import hbs.com.picnic.utils.KakaoManager
import hbs.com.picnic.utils.NicknameManager
import hbs.com.picnic.view.content.adapter.ContentAdapter
import hbs.com.picnic.view.content.presenter.ContentViewPresenter
import kotlinx.android.synthetic.main.layout_bottom_sheet.view.*
import kotlinx.android.synthetic.main.layout_coordinator_toolbar.view.*
import java.util.*

class ContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr), ContentViewContract.View {

    private val presenter by lazy {
        ContentViewPresenter(this)
    }
    private val kakaoManager by lazy { KakaoManager(context) }

    private val viewContentBinding = provideDataBinding()
    private val bottomSheetContainer = viewContentBinding.bottomSheetContainer

    private val contentMap = hashMapOf<String, ByteArray>()
    private val contentAdapter = ContentAdapter(contentMap)
    private val chattingAdapter = ChattingAdapter()

    private val topic = "hello10"

    init {
        presenter.initView()
        presenter.initBookmark(topic)
        presenter.getChatContents("0001")
    }

    fun updateMap(mapImage: ByteArray) {
        contentMap["MAP"] = mapImage
        contentAdapter.notifyItemChanged(3)
    }

    override fun initView() {
        viewContentBinding.run {
            rvContents.apply {
                adapter = contentAdapter
                layoutManager = LinearLayoutManager(context)
            }
            bottomSheetContainer.rv_chat.apply {
                adapter = chattingAdapter
                layoutManager = LinearLayoutManager(context)
            }
            srlContents.post {
                srlContents.setOnRefreshListener(refreshListener)
            }
            findActivity<AppCompatActivity>(context)?.let {
                it.setSupportActionBar(coordinatorToolbar.toolbar)
                it.supportActionBar
            }?.run {
                setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
                setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
                title = ""
            }
            viewContentBinding.coordinatorToolbar.toolbar_bookmark.setOnClickListener {
                presenter.fetchBookmark(true)
            }
            viewContentBinding.coordinatorToolbar.toolbar_share.setOnClickListener {
                kakaoManager.sendMsg("새우버거는 비가 와도 피크닉을 가요.", "https://t1.daumcdn.net/cfile/tistory/277DA93B586C7C180F")
            }
        }
    }


    override fun addTextWatcherForAnimation() {
        et_send_chat.apply {
            bottomSheetContainer.apply {
                val textWatcher =
                    presenter.makeTextWatcher(iv_send_chat_container, iv_send_chat_icon)
                addTextChangedListener(textWatcher)
            }
        }
    }


    override fun clearEditText() {
        bottomSheetContainer.et_send_chat.setText("")
    }

    override fun sendChatting() {

    }

    override fun updateChatRooms() {

    }

    override fun addSendListener(firebaseUser: FirebaseUser?) {
        val nicknameManager = NicknameManager()
        bottomSheetContainer.iv_send_chat_icon.setOnClickListener {
            val message = bottomSheetContainer.et_send_chat.text.toString()
            if (firebaseUser == null) {
                Toast.makeText(context, "네트워크 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val nickname =
                    nicknameManager.makeWording(firebaseUser.metadata?.creationTimestamp.toString())
                val chatMessage =
                    ChatMessage(firebaseUser.uid, nickname, message, Date().time.toString())
                val cloudMessage = CloudMessage(
                    topic,
                    "서현역 커피숍-" + chatMessage.name.substring(0, 6),
                    chatMessage.message,
                    firebaseUser.uid
                )
                presenter.sendChatting("0001", chatMessage)
                presenter.sendFcmMessage(cloudMessage)
            }
        }
    }

    override fun addAnimation(view: ImageView, animationType: AnimationUtils.AnimationType) {
        val scaleAnimatorListener =
            AnimationUtils.addAnimateListener(presenter, view, animationType)
        AnimationUtils.animateScale(view, scaleAnimatorListener)
    }

    override fun changeImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }


    override fun showToast(message: Any) {
        when(message){
            is String ->Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            is Int -> Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
        }
    }

    private fun provideDataBinding() =
        ViewContentBinding.inflate(LayoutInflater.from(context), this, true)

    override fun initChattingContents(chatMessages: List<ChatMessage>) {
        bottomSheetContainer.rv_chat.layoutManager?.let {
            chattingAdapter.setData(chatMessages)
            bottomSheetContainer.tv_bottom_sheet_comment_count.text = context.getString(
                R.string.all_text_layout_bottom_sheet_comment_count, chattingAdapter.itemCount
            )
            bottomSheetContainer.rv_chat.postDelayed({
                it.scrollToPosition(chatMessages.lastIndex)
            }, 200)
        } ?: run {
            return
        }
    }

    override fun updateChattingContents(chatMessages: List<ChatMessage>) {
        bottomSheetContainer.rv_chat.layoutManager?.let {
            chattingAdapter.setData(chatMessages)
            bottomSheetContainer.tv_bottom_sheet_comment_count.text = context.getString(
                R.string.all_text_layout_bottom_sheet_comment_count, chattingAdapter.itemCount
            )
            val linearLayoutManager = it as LinearLayoutManager
            if (linearLayoutManager.findLastVisibleItemPosition() == chatMessages.lastIndex - 1) {
                linearLayoutManager.scrollToPosition(chatMessages.lastIndex)
            }
        } ?: run {
            return
        }
    }

    override fun refreshContentList() {
        presenter.initView()
        viewContentBinding.srlContents.post {
            if (viewContentBinding.srlContents.isRefreshing) {
                viewContentBinding.srlContents.isRefreshing = false
            }
        }
    }

    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        viewContentBinding.srlContents.post {
            if (!viewContentBinding.srlContents.isRefreshing) {
                viewContentBinding.srlContents.isRefreshing = true
            }
            presenter.getChatContents("0001")
        }
    }

    private fun <T : Activity> findActivity(@NonNull context: Context?): T? {
        if (context == null) {
            throw IllegalArgumentException("Context cannot be null!")
        }
        return if (context is Activity) {
            context as T
        } else {
            return null
        }
    }

    override fun updateBookmark(isBookmark: Boolean) {
        if (isBookmark) {
            R.drawable.ic_bookmark_on_24dp
        } else {
            R.drawable.ic_bookmark_off_24dp
        }.run {
            viewContentBinding.coordinatorToolbar.toolbar_bookmark.setImageResource(this)
        }
    }
}