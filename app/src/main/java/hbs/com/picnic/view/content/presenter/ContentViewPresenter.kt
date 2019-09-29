package hbs.com.picnic.view.content.presenter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import hbs.com.picnic.R
import hbs.com.picnic.ui.content.usecase.ChattingUseCase
import hbs.com.picnic.ui.content.usecase.ChattingUseCaseImpl
import hbs.com.picnic.data.model.Bookmark
import hbs.com.picnic.data.model.ChatMessage
import hbs.com.picnic.data.model.CloudMessage
import hbs.com.picnic.data.model.TourInfo
import hbs.com.picnic.utils.AnimationUtils
import hbs.com.picnic.utils.BaseContract
import hbs.com.picnic.utils.NicknameManager
import hbs.com.picnic.view.content.ContentViewContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import kotlin.collections.ArrayList

class ContentViewPresenter(private val view: ContentViewContract.View) : BaseContract.Presenter(),
    ContentViewContract.Presenter {
    private var isAnimation = false

    private val bookmarkSubject = BehaviorSubject.create<TourInfo.TourItemInfo>()

    private val chattingUseCase: ChattingUseCase = ChattingUseCaseImpl()
    private val chattingList: ArrayList<ChatMessage> = arrayListOf()

    override fun initView(tourItemInfo: TourInfo.TourItemInfo) {
        val topic = tourItemInfo.contentid.toString()
        view.addTextWatcherForAnimation()
        chattingUseCase.changeSubscribeState(topic, true)
    }

    override fun sendChatting(roomId: String, chatMessage: ChatMessage) {
        chattingUseCase.postChats(roomId, chatMessage)
        view.sendChatting()
        view.clearEditText()
    }

    override fun sendFcmMessage(cloudMessage: CloudMessage) {
        chattingUseCase.sendFcmMessage(cloudMessage).subscribe({

        }, {

        })
    }

    override fun sendFcmBookmarkMessage(bookmark: Bookmark) {
        val nicknameManager = NicknameManager()
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return
        val nickname = nicknameManager.makeWording(firebaseUser.metadata?.creationTimestamp.toString())
        val chatMessage =
            ChatMessage(firebaseUser.uid, nickname, "${bookmark.title}, 즐겨찾기 했던 곳을 누군가도 좋아합니다.", Date().time.toString())
        val cloudMessage = CloudMessage(
            bookmark.uniqueId,
            bookmark.title + "에서 온 메시지",
            chatMessage.message,
            firebaseUser.uid
        )
        chattingUseCase.sendFcmMessage(cloudMessage).subscribe({

        }, {

        })
    }

    override fun getChatContents(roomId: String) {
        onClear()
        chattingList.clear()
        addDisposable(chattingUseCase.getChats(roomId).subscribe({ chatting ->
            chattingList.add(chatting)
        }, { error ->
            view.showToast(error.localizedMessage)
        },{
            view.refreshContentList()
            chattingList.reverse()
            view.initChattingContents(chattingList)
            updateChatContents(roomId)
        }))
    }

    override fun updateChatContents(roomId: String) {
        addDisposable(chattingUseCase.listenChats(roomId).subscribe({ chatting ->
            if(!chattingList.contains(chatting)){
                chattingList.add(chatting)
            }
            view.updateChattingContents(chattingList)
        }, { error ->
            view.showToast(error.localizedMessage)
        }))
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
                    toggleAnimationFlag(AnimationUtils.AnimationType.EMPTY_TO_FULL)
                } else if (textLength == 0 && isAnimation) {
                    playAnimation(backgroundView, iconView, AnimationUtils.AnimationType.FULL_TO_EMPTY)
                    toggleAnimationFlag(AnimationUtils.AnimationType.FULL_TO_EMPTY)
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

    private fun toggleAnimationFlag(flag: AnimationUtils.AnimationType) {
        isAnimation = flag == AnimationUtils.AnimationType.EMPTY_TO_FULL
    }

    override fun initBookmark(tourItemInfo: TourInfo.TourItemInfo) {
        val bookmarkDisposable = bookmarkSubject.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ tourItemInfo ->
                val bookmark = chattingUseCase.selectBookmark(tourItemInfo.contentid.toString())
                val isBookmark = bookmark?.uniqueId != null
                Log.d("bookmarkbookmark", bookmark.toString())
                Log.d("bookmarkbookmark", bookmark?.uniqueId.toString())
                Log.d("bookmarkbookmark", isBookmark.toString())
                chattingUseCase.changeSubscribeState(tourItemInfo.contentid.toString(), isBookmark)
                view.updateBookmark(isBookmark)
            }, { error ->
                error.printStackTrace()
            }, {

            })
        //검색
    }

    //TODO : LOCAL REPOSITORY를 연결해서 bookmark 지정
    override fun fetchBookmark(tourItemInfo: TourInfo.TourItemInfo) {
        bookmarkSubject.onNext(tourItemInfo)
    }

    override fun insertBookmark(bookmark: Bookmark) {
        chattingUseCase.insertBookmark(bookmark)
    }

    override fun deleteBookmark(bookmark: Bookmark) {
        chattingUseCase.removeBookmark(bookmark)
    }

    override fun getBookmark(bookmarkId: String): Bookmark? {
        return chattingUseCase.selectBookmark(bookmarkId)
    }
}