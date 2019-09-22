package hbs.com.picnic.view.content.presenter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import hbs.com.picnic.R
import hbs.com.picnic.content.usecase.ChattingUseCase
import hbs.com.picnic.content.usecase.ChattingUseCaseImpl
import hbs.com.picnic.data.model.Bookmark
import hbs.com.picnic.data.model.ChatMessage
import hbs.com.picnic.data.model.CloudMessage
import hbs.com.picnic.utils.AnimationUtils
import hbs.com.picnic.utils.BaseContract
import hbs.com.picnic.view.content.ContentViewContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ContentViewPresenter(private val view: ContentViewContract.View) : BaseContract.Presenter(),
    ContentViewContract.Presenter {
    private var isAnimation = false

    private val bookmarkSubject = BehaviorSubject.create<Boolean>()

    private val chattingUseCase: ChattingUseCase = ChattingUseCaseImpl()
    private val chattingList: ArrayList<ChatMessage> = arrayListOf()
    override fun initView() {
        view.initView()
        view.addTextWatcherForAnimation()
        chattingUseCase.changeSubscribeState("hello10", true)
    }

    override fun sendChatting(roomId: String, chatMessage: ChatMessage) {
        chattingUseCase.postChats(roomId, chatMessage)
        view.sendChatting()
        view.clearEditText()
    }

    override fun sendFcmMessage(cloudMessage: CloudMessage) {
        chattingUseCase.sendFcmMessage(cloudMessage).subscribe({
            fetchBookmark(true)
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

    override fun initBookmark(topic: String) {
        val bookmarkDisposable = bookmarkSubject.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
            .subscribe({ isBookmark ->
                view.updateBookmark(isBookmark)
                val bookmark = chattingUseCase.selectBookmark(Bookmark("","","","1",isBookmark))
                if (bookmark!=null && !bookmark.isBookmark) {
                    chattingUseCase.changeSubscribeState(topic, isBookmark)
                    view.showToast(R.string.all_text_register_bookmark)
                }
            }, { error ->
                error.printStackTrace()
            }, {

            })
    }

    //TODO : LOCAL REPOSITORY를 연결해서 bookmark 지정
    override fun fetchBookmark(isBookmark: Boolean) {
        bookmarkSubject.onNext(isBookmark)
    }

    override fun insertBookmark(bookmark: Bookmark) {
        chattingUseCase.insertBookmark(bookmark)
    }
}