package hbs.com.picnic.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import hbs.com.picnic.data.model.CloudMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

interface FcmRepository{
    fun sendMessage(cloudMessage: CloudMessage): Observable<ResponseBody>
    fun addFavoritePlace(topic: String): Task<Void>
}

class FcmRepositoryImpl(private val fcmAPI: FcmAPI) : FcmRepository{
    override fun sendMessage(cloudMessage: CloudMessage): Observable<ResponseBody> =
        cloudMessage.run {
            fcmAPI.sendMessageToTopic(topic, messageTitle, message, uid)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
        }


    override fun addFavoritePlace(topic: String) = FirebaseMessaging.getInstance().subscribeToTopic("hello")
}