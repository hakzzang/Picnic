package hbs.com.picnic.remote

import hbs.com.picnic.data.model.CloudMessage
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface FcmAPI{
    @Headers("Content-Type: application/json", MapAPI.API.SENDER_ID)
    @POST("send")
    fun sendMessageToTopic(@Body cloudMessage: CloudMessage) : Observable<ResponseBody>
}