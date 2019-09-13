package hbs.com.picnic.view.content

import android.text.TextWatcher
import android.widget.ImageView
import hbs.com.picnic.data.model.ChatMessage
import hbs.com.picnic.utils.AnimationUtils

interface ContentViewContract{
    interface View{
        fun initView()
        fun updateChatRooms()
        fun updateChattingContents(chatMessage: List<ChatMessage>)
        fun sendChatting()
        fun addSendListener()
        fun clearEditText()
        fun addTextWatcherForAnimation()
        fun showFailToastMessage(failMessage: String)
        fun addAnimation(view: ImageView, animationType: AnimationUtils.AnimationType)
        fun changeImageResource(imageView: ImageView, resource: Int)
        fun refreshContentList()
    }

    interface Presenter{
        fun getChatContents(roomId: String)
        fun getChatRooms()
        fun sendChatting(roomId: String, chatMessage: ChatMessage)
        fun initView()
        fun makeTextWatcher(backgroundView: ImageView, iconView: ImageView): TextWatcher
        fun changeImageResource(imageView: ImageView, animationType: AnimationUtils.AnimationType)
    }
}