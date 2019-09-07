package hbs.com.picnic.view.presenter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import hbs.com.picnic.R
import hbs.com.picnic.utils.AnimationUtils
import hbs.com.picnic.view.ContentViewContract

class ContentViewPresenter(private val view: ContentViewContract.View) : ContentViewContract.Presenter {
    private var isAnimation = false
    override fun initView() {
        view.initView()
        view.addSendListener()
        view.addTextWatcherForAnimation()
    }

    override fun sendChatting() {
        view.sendChatting()
        view.clearEditText()
    }

    override fun getChatContents() {

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