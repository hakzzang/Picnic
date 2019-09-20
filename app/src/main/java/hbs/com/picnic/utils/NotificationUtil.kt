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
import hbs.com.picnic.content.ContentActivity


class NotificationUtil {
    val NOTIFICATION_CHANNEL_ID = "PICNIC1000"
    fun send(context: Context) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?

        val notificationIntent = Intent(context, ContentActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("상태바 드래그시 보이는 타이틀")
            .setContentText("상태바 드래그시 보이는 서브타이틀")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "노티페케이션 채널"
            val description = "오레오 이상을 위한 것임"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
            channel.description = description

            // 노티피케이션 채널을 시스템에 등록
            assert(notificationManager != null)
            notificationManager!!.createNotificationChannel(channel)

        } else {
            builder.setSmallIcon(R.mipmap.sym_def_app_icon) // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
        }


        notificationManager!!.notify(1234, builder.build()) // 고유숫자로 노티피케이션 동작시킴

    }
}