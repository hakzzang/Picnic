package hbs.com.picnic.remote

import hbs.com.picnic.data.model.CloudMessage
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface FcmAPI{
    @Headers(MapAPI.API.SENDER_ID, "Content-Type: application/json")
    @POST("send")
    fun sendMessageToTopic(@Body cloudMessage: CloudMessage) : Observable<ResponseBody>
}