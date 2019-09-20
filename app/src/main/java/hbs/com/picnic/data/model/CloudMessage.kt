package hbs.com.picnic.data.model

import com.google.gson.annotations.SerializedName

data class CloudMessage(
    @SerializedName("topic") var topic: String,
    @SerializedName("notification") var notification: Notification
)

data class Notification(@SerializedName("body") var body: String,
                        @SerializedName("title") var title: String)