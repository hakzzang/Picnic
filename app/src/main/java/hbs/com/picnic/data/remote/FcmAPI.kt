package hbs.com.picnic.data.remote

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface FcmAPI{
    @Headers("Content-Type: application/json")
    @GET("chat/notification")
    fun sendMessageToTopic(
        @Query("topic") topic: String,
        @Query("message") message: String,
        @Query("messageTitle") messageTitle: String,
        @Query("uid") uid: String
    ): Observable<ResponseBody>
}