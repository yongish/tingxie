package com.zhiyong.tingxie

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zhiyong.tingxie.network.NetworkQuiz
import com.zhiyong.tingxie.network.NetworkToken
import com.zhiyong.tingxie.network.TingXieNetwork
import com.zhiyong.tingxie.ui.main.MainActivity
import com.zhiyong.tingxie.ui.question.RemoteQuestionActivity
import com.zhiyong.tingxie.ui.word.WordActivity
import kotlinx.coroutines.runBlocking


class MyFirebaseMessagingService : FirebaseMessagingService() {

  override fun onMessageReceived(p0: RemoteMessage) {
    super.onMessageReceived(p0)
    sendNotification(p0)
  }

  private fun sendNotification(p0: RemoteMessage) {
    val activity = when (p0.data["action"]) {
      "" -> MainActivity::class.java
      else -> MainActivity::class.java
    }
//    val intent = Intent(this, activity)

    val intent = Intent(this, WordActivity::class.java)
    intent.putExtra(
      RemoteQuestionActivity.EXTRA_QUIZ_ITEM,
      NetworkQuiz(
        p0.data["entityId"]?.toLong() ?: -1,
        p0.data["title"] ?: "NO TITLE",
        p0.data["date"]?.toInt() ?: -1,
        p0.data["email"] ?: "NO EMAIL",
        p0.data["role"] ?: "NO ROLE",
        p0.data["numWords"]?.toInt() ?: -1,
        p0.data["numNotCorrect"]?.toInt() ?: -1,
        p0.data["round"]?.toInt() ?: -1
      )
    )

    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent =
      PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val channelId = "fcm_default_channel"
    val defaultSoundUri =
      RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
      .setContentTitle(p0.notification?.title)
      .setSmallIcon(R.drawable.icon)
      .setContentText(p0.notification?.body)
      .setStyle(NotificationCompat.BigTextStyle().bigText(p0.notification?.body))
      .setAutoCancel(true)
      .setSound(defaultSoundUri)
      .setContentIntent(pendingIntent)

    val notificationManager =
      getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
  }

  override fun onNewToken(p0: String) {
    super.onNewToken(p0)

    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid
    val email = currentUser?.email
    if (uid != null && email != null) {
      try {
        runBlocking {
          TingXieNetwork.tingxie.putToken(NetworkToken(uid, email, p0))
        }
      } catch (e: Exception) {
        // todo: Log to Crashlytics?
        Log.e("REFRESH TOKEN FAILED", e.message.orEmpty())
      }
    }
  }
}