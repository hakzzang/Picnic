package hbs.com.picnic.remote

import hbs.com.picnic.data.model.CloudMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

interface FcmRepository{
    fun sendMessage(topic:String, cloudMessage: CloudMessage):Observable<ResponseBody>
}

class FcmRepositoryImpl(val fcmAPI: FcmAPI) : FcmRepository{
    override fun sendMessage(topic: String, cloudMessage: CloudMessage): Observable<ResponseBody> =
        fcmAPI.sendMessageToTopic(cloudMessage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
}