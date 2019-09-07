package hbs.com.picnic.view

import android.text.TextWatcher
import android.widget.ImageView
import hbs.com.picnic.utils.AnimationUtils

interface ContentViewContract{
    interface View{
        fun initView()
        fun updateChatRooms()
        fun sendChatting()
        fun addSendListener()
        fun clearEditText()
        fun addTextWatcherForAnimation()
        fun addAnimation(view: ImageView, animationType: AnimationUtils.AnimationType)
        fun changeImageResource(imageView: ImageView, resource: Int)
    }

    interface Presenter{
        fun getChatContents()
        fun getChatRooms()
        fun sendChatting()
        fun initView()
        fun makeTextWatcher(backgroundView: ImageView, iconView: ImageView): TextWatcher
        fun changeImageResource(imageView: ImageView, animationType: AnimationUtils.AnimationType)
    }
}