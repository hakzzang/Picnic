package hbs.com.picnic.view.content

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import hbs.com.picnic.content.adapter.ChattingAdapter
import hbs.com.picnic.data.model.ChatMessage
import hbs.com.picnic.databinding.ViewContentBinding
import hbs.com.picnic.utils.AnimationUtils
import hbs.com.picnic.view.content.adapter.ContentAdapter
import hbs.com.picnic.view.content.presenter.ContentViewPresenter
import kotlinx.android.synthetic.main.layout_bottom_sheet.view.*
import java.util.*

class ContentView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr), ContentViewContract.View {

    private val presenter by lazy {
        ContentViewPresenter(this)
    }
    private val viewContentBinding = provideDataBinding()
    private val bottomSheetContainer = viewContentBinding.bottomSheetContainer

    private val contentMap = hashMapOf<String, ByteArray>()
    private val contentAdapter = ContentAdapter(contentMap)
    private val chattingAdapter = ChattingAdapter()
    init {
        presenter.initView()
    }

    fun updateMap(mapImage : ByteArray){
        contentMap["MAP"] = mapImage
        contentAdapter.notifyItemChanged(3)
    }

    override fun initView() {
        viewContentBinding.let {
            it.rvContents.apply {
                adapter = contentAdapter
                layoutManager = LinearLayoutManager(context)
            }
            it.bottomSheetContainer.rv_chat.apply {
                adapter = chattingAdapter
                layoutManager = LinearLayoutManager(context)
            }
            it.srlContents.post {
                it.srlContents.setOnRefreshListener(refreshListener)
            }
        }
    }

    override fun addTextWatcherForAnimation() {
        et_send_chat.apply {
            bottomSheetContainer.apply{
                val textWatcher = presenter.makeTextWatcher(iv_send_chat_container, iv_send_chat_icon)
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

    override fun addSendListener() {
        bottomSheetContainer.iv_send_chat_icon.setOnClickListener {
            val message = bottomSheetContainer.et_send_chat.text.toString()
            val chatMessage = ChatMessage("hakzzang", "hakzzang", message, Date().time.toString())
            presenter.sendChatting("0001", chatMessage)
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


    override fun showFailToastMessage(failMessage: String) {
        Toast.makeText(context, failMessage, Toast.LENGTH_SHORT).show()
    }

    private fun provideDataBinding() = ViewContentBinding.inflate(LayoutInflater.from(context), this, true)

    override fun updateChattingContents(chatMessages: List<ChatMessage>) {
        chattingAdapter.setData(chatMessages)
    }

    override fun refreshContentList() {
        presenter.initView()
        viewContentBinding.srlContents.post {
            if(viewContentBinding.srlContents.isRefreshing){
                viewContentBinding.srlContents.isRefreshing = false
            }
        }
    }

    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        viewContentBinding.srlContents.post {
            if(!viewContentBinding.srlContents.isRefreshing){
                viewContentBinding.srlContents.isRefreshing = true
            }
            presenter.getChatContents("0001")
        }
    }
}