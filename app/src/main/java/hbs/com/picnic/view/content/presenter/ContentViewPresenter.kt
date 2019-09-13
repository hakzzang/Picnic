package hbs.com.picnic.view.content.presenter

import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import hbs.com.picnic.R
import hbs.com.picnic.content.usecase.ChattingUseCase
import hbs.com.picnic.content.usecase.ChattingUseCaseImpl
import hbs.com.picnic.data.model.ChatMessage
import hbs.com.picnic.utils.AnimationUtils
import hbs.com.picnic.utils.BaseContract
import hbs.com.picnic.view.content.ContentViewContract

class ContentViewPresenter(private val view: ContentViewContract.View) : BaseContract.Presenter(),
    ContentViewContract.Presenter {
    private var isAnimation = false
    private val chattingUseCase: ChattingUseCase = ChattingUseCaseImpl()
    private val chattingList: ArrayList<ChatMessage> = arrayListOf()
    override fun initView() {
        view.initView()
        view.addSendListener()
        view.addTextWatcherForAnimation()
    }

    override fun sendChatting(roomId: String, chatMessage: ChatMessage) {
        chattingUseCase.postChats(roomId, chatMessage)
        view.sendChatting()
        view.clearEditText()
    }

    override fun getChatContents(roomId: String) {
        addDisposable(chattingUseCase.getChats(roomId).subscribe({ chatting ->
            chattingList.add(chatting)
            view.updateChattingContents(chattingList)
            view.refreshContentList()
        }, { error ->
            view.showFailToastMessage(error.localizedMessage)
        }))
    }

    override fun getChatRooms() {

    }

    override fun makeTextWatcher(backgroundView: ImageView, iconView: ImageView): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textLength = s?.length
                if (textLength == 1 && !isAnimation) {
                    playAnimation(backgroundView, iconView, AnimationUtils.AnimationType.EMPTY_TO_FULL)
                    changeFlag(AnimationUtils.AnimationType.EMPTY_TO_FULL)
                    isAnimation = true
                } else if (textLength == 0 && isAnimation) {
                    playAnimation(backgroundView, iconView, AnimationUtils.AnimationType.FULL_TO_EMPTY)
                    changeFlag(AnimationUtils.AnimationType.FULL_TO_EMPTY)
                    isAnimation = false
                }
            }
        }
    }

    override fun changeImageResource(imageView: ImageView, animationType: AnimationUtils.AnimationType) {
        when (imageView.id) {
            R.id.iv_send_chat_container -> {
                if (animationType == AnimationUtils.AnimationType.EMPTY_TO_FULL) {
                    view.changeImageResource(imageView, R.drawable.bg_send_chat_on)
                } else {
                    view.changeImageResource(imageView, R.drawable.bg_send_chat_off)
                }
            }
            R.id.iv_send_chat_icon -> {
                if (animationType == AnimationUtils.AnimationType.EMPTY_TO_FULL) {
                    view.changeImageResource(imageView, R.drawable.ic_send_button_on)
                } else {
                    view.changeImageResource(imageView, R.drawable.ic_send_button_off)
                }
            }
        }
    }

    private fun playAnimation(
        backgroundView: ImageView,
        iconView: ImageView,
        flag: AnimationUtils.AnimationType
    ) {
        view.addAnimation(backgroundView, flag)
        view.addAnimation(iconView, flag)
    }

    private fun changeFlag(flag: AnimationUtils.AnimationType) {
        isAnimation = flag == AnimationUtils.AnimationType.EMPTY_TO_FULL
    }
}