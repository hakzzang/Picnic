package hbs.com.picnic.ui.content.usecase

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import hbs.com.picnic.data.local.LocalRepositoryImpl
import hbs.com.picnic.data.model.Bookmark
import hbs.com.picnic.data.model.ChatMessage
import hbs.com.picnic.data.model.CloudMessage
import hbs.com.picnic.data.remote.ChatRepository
import hbs.com.picnic.data.remote.ChatRepositoryImpl
import hbs.com.picnic.data.remote.FcmRepositoryImpl
import hbs.com.picnic.data.remote.RetrofitProvider
import hbs.com.picnic.utils.BaseUrl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import okhttp3.ResponseBody


interface ChattingUseCase {
    fun getChats(roomId: String): Observable<ChatMessage>
    fun listenChats(roomId: String) : Observable<ChatMessage>
    fun postChats(roomId: String, chatMessage: ChatMessage)
    fun sendFcmMessage(cloudMessage: CloudMessage): Observable<ResponseBody>
    fun changeSubscribeState(topic:String, isSubscribe:Boolean)
    fun insertBookmark(bookmark: Bookmark)
    fun selectBookmark(bookmark: Bookmark): Bookmark?
}

class ChattingUseCaseImpl : ChattingUseCase {
    private val chatRepository: ChatRepository = ChatRepositoryImpl()
    private val fcmFcmRepository = FcmRepositoryImpl(RetrofitProvider.provideFcmApi(BaseUrl.PICNIC_SERVER.url))
    private val localRepository = LocalRepositoryImpl<Bookmark>()
    private var chatReceiver: ListenerRegistration? = null
    override fun postChats(roomId: String, chatMessage: ChatMessage) {
        chatRepository.postChats(roomId, chatMessage)
    }

    override fun getChats(roomId: String): Observable<ChatMessage> {
        val chattingPublishSubject: BehaviorSubject<ChatMessage> = BehaviorSubject.create()
        chatRepository.getChats(roomId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(25)
            .get().addOnSuccessListener {
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
        val chattingListenPublishSubject = BehaviorSubject.create<ChatMessage>()
        chatReceiver?.remove()
        chatReceiver = chatRepository.getChats(roomId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.d("ERROR", firebaseFirestoreException.message);
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && !documentSnapshot.isEmpty) {
                    documentSnapshot.documentChanges.apply {
                        for (doc in this) {
                            if (doc.type == DocumentChange.Type.ADDED) {
                                val chatting =
                                    doc.document.toObject(hbs.com.picnic.data.model.ChatMessage::class.java)
                                chattingListenPublishSubject.onNext(chatting)
                            }
                        }
                    }
                }
            }

        return chattingListenPublishSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
    }

    override fun sendFcmMessage(cloudMessage: CloudMessage): Observable<ResponseBody> {
        return fcmFcmRepository
            .sendMessage(cloudMessage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
    }

    override fun changeSubscribeState(topic: String, isSubscribe: Boolean) {
        if(isSubscribe){
            fcmFcmRepository.subscribePlaceNews(topic)
        }else{
            fcmFcmRepository.unSubscribePlaceNews(topic)
        }
    }

    override fun insertBookmark(bookmark: Bookmark) {
        localRepository.realm.beginTransaction()
        localRepository.upsert(localRepository.realm, bookmark)
        localRepository.realm.commitTransaction()

    }

    override fun selectBookmark(bookmark: Bookmark): Bookmark? {
        localRepository.realm.beginTransaction()
        val resultBookmark = localRepository.select(localRepository.realm, bookmark)
        localRepository.realm.commitTransaction()
        return resultBookmark
    }
}