package hbs.com.picnic.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import hbs.com.picnic.R

class FcmService : FirebaseMessagingService(){
    private val notificationUtil = NotificationUtil()
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.data["uid"] == FirebaseAuth.getInstance().currentUser?.uid){
            //uid 비교로 로직처리
        }

        notificationUtil.send(this, remoteMessage, R.drawable.ic_foodtruck_line)
    }
}