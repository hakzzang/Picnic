package hbs.com.picnic.data.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import hbs.com.picnic.data.model.CloudMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

interface FcmRepository{
    fun sendMessage(cloudMessage: CloudMessage): Observable<ResponseBody>
    fun subscribePlaceNews(topic: String): Task<Void>
    fun unSubscribePlaceNews(topic: String): Task<Void>
}

class FcmRepositoryImpl(private val fcmAPI: FcmAPI) : FcmRepository{
    override fun sendMessage(cloudMessage: CloudMessage): Observable<ResponseBody> =
        cloudMessage.run {
            fcmAPI.sendMessageToTopic(topic, messageTitle, message, uid)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
        }


    override fun subscribePlaceNews(topic: String) =
        FirebaseMessaging.getInstance().subscribeToTopic(topic)

    override fun unSubscribePlaceNews(topic: String): Task<Void> =
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
}