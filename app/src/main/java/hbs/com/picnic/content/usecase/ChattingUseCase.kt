package hbs.com.picnic.content.usecase

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldPath.documentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.messaging.FirebaseMessaging
import hbs.com.picnic.MainActivity
import hbs.com.picnic.data.model.ChatMessage
import hbs.com.picnic.data.model.CloudMessage
import hbs.com.picnic.remote.ChatRepository
import hbs.com.picnic.remote.ChatRepositoryImpl
import hbs.com.picnic.remote.FcmRepositoryImpl
import hbs.com.picnic.remote.RetrofitProvider
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
}

class ChattingUseCaseImpl : ChattingUseCase {
    private val chatRepository: ChatRepository = ChatRepositoryImpl()
    private val fcmFcmRepository = FcmRepositoryImpl(RetrofitProvider.provideFcmApi(BaseUrl.PICNIC_SERVER.url))

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
        fcmFcmRepository.subscribePlaceNews(cloudMessage.topic)
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
}