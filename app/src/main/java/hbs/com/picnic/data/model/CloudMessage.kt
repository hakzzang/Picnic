package hbs.com.picnic.data.model

import com.google.gson.annotations.SerializedName

data class CloudMessage(
    @SerializedName("topic") var topic: String,
    @SerializedName("message") var message: String,
    @SerializedName("messageTitle") var messageTitle: String,
    @SerializedName("uid") var uid: String
)