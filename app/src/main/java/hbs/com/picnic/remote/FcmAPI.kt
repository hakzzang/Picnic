package hbs.com.picnic.remote

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.gson.annotations.SerializedName
import hbs.com.picnic.data.model.CloudMessage
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