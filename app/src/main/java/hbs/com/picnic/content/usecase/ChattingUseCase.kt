package hbs.com.picnic.content.usecase

import hbs.com.picnic.data.model.ChatMessage
import hbs.com.picnic.remote.ChatRepository
import hbs.com.picnic.remote.ChatRepositoryImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject


interface ChattingUseCase {
    fun getChats(roomId: String): Observable<ChatMessage>
    fun listenChats(roomId: String) : Observable<ChatMessage>
    fun postChats(roomId: String, chatMessage: ChatMessage)
}

class ChattingUseCaseImpl : ChattingUseCase {
    private val chattingPublishSubject: BehaviorSubject<ChatMessage> = BehaviorSubject.create()

    private val chatRepository: ChatRepository = ChatRepositoryImpl()
    override fun postChats(roomId: String, chatMessage: ChatMessage) {
        chatRepository.postChats(roomId, chatMessage)
    }

    override fun getChats(roomId: String): Observable<ChatMessage> {
        chatRepository.getChats(roomId).get().addOnSuccessListener {
            it.documentChanges.let {
                for (doc in it) {
                    ChatMessage().apply {
                        convertMapTo(doc.document.data)
                    }.let {
                        chattingPublishSubject.onNext(it)
                    }
                }
                chattingPublishSubject.onComplete()
            }
        }

        return chattingPublishSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
    }

    override fun listenChats(roomId: String): Observable<ChatMessage> {
        return 
    }
}