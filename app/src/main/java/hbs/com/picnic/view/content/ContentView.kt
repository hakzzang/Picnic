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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseUser
import hbs.com.picnic.R
import hbs.com.picnic.data.model.*
import hbs.com.picnic.databinding.ViewContentBinding
import hbs.com.picnic.ui.content.adapter.ChattingAdapter
import hbs.com.picnic.utils.AnimationUtils
import hbs.com.picnic.utils.KakaoManager
import hbs.com.picnic.utils.NicknameManager
import hbs.com.picnic.view.content.adapter.ContentAdapter
import hbs.com.picnic.view.content.presenter.ContentViewPresenter
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
    private var tourItemInfo: TourInfo.TourItemInfo? = null
    private var tourDetail: TourDetail? = null
    private val chattingAdapter = ChattingAdapter()

    fun updateMap(mapImage: ByteArray) {
        contentMap["MAP"] = mapImage
        viewContentBinding.rvContents.adapter?.notifyItemChanged(3)
    }

    fun initTourItemInfo(tourItemInfo: TourInfo.TourItemInfo){
        this.tourItemInfo = tourItemInfo
    }
    override fun initView(tourItemInfo: TourInfo.TourItemInfo) {
        viewContentBinding.run {
            rvContents.apply {
                adapter = ContentAdapter(tourItemInfo, tourDetail, contentMap)
                layoutManager = LinearLayoutManager(context)
            }
            bottomSheetContainer.rvChat.apply {
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
            coordinatorToolbar.toolbarBookmark.setOnClickListener {
                val bookmark = Bookmark(
                    tourItemInfo.title,
                    tourItemInfo.firstimage,
                    System.currentTimeMillis().toString(),
                    tourItemInfo.contentid.toString(),
                    false
                )
                val resultBookmark = presenter.getBookmark(bookmark.uniqueId)
                val isBookmark = resultBookmark?.uniqueId != null
                bookmark.isBookmark = isBookmark
                presenter.fetchBookmark(tourItemInfo)

                if (isBookmark) {
                    presenter.deleteBookmark(bookmark)
                } else {
                    presenter.sendFcmBookmarkMessage(bookmark)
                    presenter.insertBookmark(bookmark)
                }
            }
            coordinatorToolbar.toolbarShare.setOnClickListener {
                kakaoManager.sendMsg(tourItemInfo.title, tourItemInfo.firstimage)
            }
            viewContentBinding.tourItemInfo = tourItemInfo
        }
        presenter.initView(tourItemInfo)
        presenter.initBookmark(tourItemInfo)
        val resultBookmark = presenter.getBookmark(tourItemInfo.contentid.toString())
        val isBookmark = resultBookmark?.uniqueId != null
        if (isBookmark) {
            updateBookmark(isBookmark)
        }
    }

    fun getChatContents(tourItemInfo: TourInfo.TourItemInfo){
        presenter.getChatContents(tourItemInfo.contentid.toString())
    }

    override fun addTextWatcherForAnimation() {
        bottomSheetContainer.etSendChat.apply {
            val textWatcher = presenter.makeTextWatcher(
                bottomSheetContainer.ivSendChatContainer,
                bottomSheetContainer.ivSendChatIcon
            )
            addTextChangedListener(textWatcher)
        }
    }

    override fun clearEditText() {
        bottomSheetContainer.etSendChat.setText("")
    }

    override fun sendChatting() {

    }

    override fun updateChatRooms() {

    }

    override fun addSendListener(firebaseUser: FirebaseUser?) {
        val nicknameManager = NicknameManager()
        bottomSheetContainer.ivSendChatContainer.setOnClickListener {
            val message = bottomSheetContainer.etSendChat.text.toString()
            if (firebaseUser == null) {
                Toast.makeText(context, "네트워크 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val nickname =
                    nicknameManager.makeWording(firebaseUser.metadata?.creationTimestamp.toString())
                val chatMessage =
                    ChatMessage(firebaseUser.uid, nickname, message, Date().time.toString())
                val cloudMessage = CloudMessage(
                    tourItemInfo?.contentid.toString(),
                    tourItemInfo?.title + "에서 " + chatMessage.name.substring(0, 6),
                    chatMessage.message,
                    firebaseUser.uid
                )
                presenter.sendChatting(tourItemInfo?.contentid.toString(), chatMessage)
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
        bottomSheetContainer.rvChat.layoutManager?.let {
            chattingAdapter.setData(chatMessages)
            bottomSheetContainer.tvBottomSheetCommentCount.text = context.getString(
                R.string.all_text_layout_bottom_sheet_comment_count, chattingAdapter.itemCount
            )
            bottomSheetContainer.rvChat.postDelayed({
                it.scrollToPosition(chatMessages.lastIndex)
            }, 200)
        } ?: run {
            return
        }
    }

    override fun updateChattingContents(chatMessages: List<ChatMessage>) {
        bottomSheetContainer.rvChat.layoutManager?.let {
            chattingAdapter.setData(chatMessages)
            bottomSheetContainer.tvBottomSheetCommentCount.text = context.getString(
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
        tourItemInfo?.let { initView(it) }
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
            presenter.getChatContents(
                tourItemInfo?.contentid.toString()
            )
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
            viewContentBinding.coordinatorToolbar.toolbarBookmark.setImageResource(this)
        }
    }

    override fun updateDetailInfo(tourDetail: TourDetail) {
        this.tourDetail = tourDetail

        if(viewContentBinding.rvContents.adapter is ContentAdapter){
            (viewContentBinding.rvContents.adapter as ContentAdapter).updateTourDetail()
        }
    }
}