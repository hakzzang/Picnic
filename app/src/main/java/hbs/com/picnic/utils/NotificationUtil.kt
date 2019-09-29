package hbs.com.picnic.utils

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import hbs.com.picnic.ui.bookmark.BookmarkActivity
import hbs.com.picnic.ui.content.ContentActivity


class NotificationUtil {
    val NOTIFICATION_CHANNEL_ID = "PICNIC1000"
    fun send(context: Context, remoteMessage: RemoteMessage, iconDrawable:Int) {

        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?

        val notificationIntent = Intent(context, BookmarkActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val remoteMessage = remoteMessage.notification?:return
        val title = remoteMessage.title?:return
        val content = remoteMessage.body?:return
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "PICNIC"
            val description = "피크닉 어플리케이션 관련 노티피케이션"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
            channel.description = description

            // 노티피케이션 채널을 시스템에 등록
            assert(notificationManager != null)
            notificationManager!!.createNotificationChannel(channel)
            builder.setSmallIcon(iconDrawable)
        } else {
            builder.setSmallIcon(R.mipmap.sym_def_app_icon) // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
        }


        notificationManager!!.notify(1234, builder.build()) // 고유숫자로 노티피케이션 동작시킴

    }
}