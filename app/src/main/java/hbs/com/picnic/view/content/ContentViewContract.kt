package hbs.com.picnic.view.content

import android.text.TextWatcher
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.auth.FirebaseUser
import hbs.com.picnic.data.model.*
import hbs.com.picnic.utils.AnimationUtils

interface ContentViewContract{
    interface View{
        fun initView(tourItemInfo: TourInfo.TourItemInfo)
        fun initChattingContents(chatMessages: List<ChatMessage>)

        fun updateChatRooms()
        fun updateChattingContents(chatMessages: List<ChatMessage>)
        fun updateBookmark(isBookmark: Boolean)
        fun changeImageResource(imageView: ImageView, resource: Int)
        fun sendChatting()
        fun clearEditText()

        fun addSendListener(firebaseUser: FirebaseUser?)
        fun addTextWatcherForAnimation()
        fun addAnimation(view: ImageView, animationType: AnimationUtils.AnimationType)

        fun showToast(message: Any)
        fun refreshContentList()
        fun updateDetailInfo(tourDetail:TourDetail)
    }

    interface Presenter{
        fun initView(tourItemInfo: TourInfo.TourItemInfo)
        fun getChatContents(roomId: String)
        fun getChatRooms()

        fun sendChatting(roomId: String, chatMessage: ChatMessage)
        fun updateChatContents(roomId: String)
        fun sendFcmMessage(cloudMessage: CloudMessage)

        fun initBookmark(tourItemInfo: TourInfo.TourItemInfo)
        fun fetchBookmark(isBookmark: Boolean)
        fun makeTextWatcher(backgroundView: ImageView, iconView: ImageView): TextWatcher
        fun changeImageResource(imageView: ImageView, animationType: AnimationUtils.AnimationType)

        fun insertBookmark(bookmark: Bookmark)
    }
}