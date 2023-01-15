package com.zhiyong.tingxie

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zhiyong.tingxie.ui.main.MainActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {

  override fun onMessageReceived(p0: RemoteMessage) {
    super.onMessageReceived(p0)

    // key-value pairs, in case we want to use them.
//    p0.data

//    p0.notification

    Handler(Looper.getMainLooper()).post {
      Toast.makeText(applicationContext, "Quiz shared with you.", Toast.LENGTH_SHORT).show()
    }

    with (p0.notification?.title?.uppercase() ?: "") {
      when {
        contains("QUIZ SHARED") -> sendNotification("hello")
        else -> ""
      }

    }
  }

  private fun sendNotification(messageBody: String) {
    val intent = Intent(this, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
      PendingIntent.FLAG_IMMUTABLE)

    val channelId = "fcm_default_channel"
    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
      .setContentTitle("FCM Message")
      .setSmallIcon(R.drawable.icon)
      .setContentText(messageBody)
      .setAutoCancel(true)
      .setSound(defaultSoundUri)
      .setContentIntent(pendingIntent)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
  }

//  override fun onNewToken(p0: String) {
//    super.onNewToken(p0)
//
//    val currentUser = FirebaseAuth.getInstance().currentUser
//    val uid = currentUser?.uid
//    val email = currentUser?.email
//    if (uid != null && email != null) {
//      try {
////        TingXieNetwork.tingxie.putToken(NetworkToken(uid, email, p0))
//      } catch (e: Exception) {
//        // todo: Log to Crashlytics?
//        Log.e("REFRESH TOKEN FAILED", e.message.orEmpty())
//      }
//    }
//  }
}