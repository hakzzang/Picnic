package hbs.com.picnic.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage



class FcmService : FirebaseMessagingService(){
    private val notificationUtil = NotificationUtil()
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("hello", "From: " + remoteMessage.from!!)

        if (remoteMessage.data.size > 0) {
            Log.d("hello", "Message data payload: " + remoteMessage.data)

        }

        if (remoteMessage.notification != null) {
            Log.d("hello", "Message Notification Body: " + remoteMessage.notification!!.body!!)
        }

        notificationUtil.send(this)

    }
}