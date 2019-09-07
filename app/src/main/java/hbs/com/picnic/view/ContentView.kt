package hbs.com.picnic.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import hbs.com.picnic.content.adapter.ChattingAdapter
import hbs.com.picnic.databinding.ViewContentBinding
import hbs.com.picnic.utils.AnimationUtils
import hbs.com.picnic.view.presenter.ContentViewPresenter
import kotlinx.android.synthetic.main.layout_bottom_sheet.view.*

class ContentView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr), ContentViewContract.View {
    private val presenter by lazy {
        ContentViewPresenter(this)
    }

    private val viewContentBinding = provideDataBinding()
    private val bottomSheetContainer = viewContentBinding.bottomSheetContainer
    private val chattingList: ArrayList<String> = arrayListOf()
    init {
        presenter.initView()
    }

    override fun initView() {
        viewContentBinding.let {
            it.rvContents.apply {
                adapter = ContentAdapter()
                layoutManager = LinearLayoutManager(context)
            }
            it.bottomSheetContainer.rv_chat.apply {
                adapter = ChattingAdapter(chattingList)
                layoutManager = LinearLayoutManager(context)
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
        chattingList.add(et_send_chat.text.toString())
    }

    override fun updateChatRooms() {

    }

    override fun addSendListener() {
        bottomSheetContainer.iv_send_chat_icon.setOnClickListener {
            presenter.sendChatting()
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

    private fun provideDataBinding() = ViewContentBinding.inflate(LayoutInflater.from(context), this, true)
}