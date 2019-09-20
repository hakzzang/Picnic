package hbs.com.picnic.data.model

import com.google.gson.annotations.SerializedName

data class CloudMessage(
    @SerializedName("to") var topic: String,
    @SerializedName("notification") var notification: Notification
)

data class Notification(@SerializedName("message") var message: String,
                        @SerializedName("title") var title: String)