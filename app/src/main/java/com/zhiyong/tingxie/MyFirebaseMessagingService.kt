package com.zhiyong.tingxie

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zhiyong.tingxie.network.NetworkQuiz
import com.zhiyong.tingxie.network.NetworkToken
import com.zhiyong.tingxie.network.TingXieNetwork
import com.zhiyong.tingxie.ui.EXTRA_USER_ROLE
import com.zhiyong.tingxie.ui.UserRole
import com.zhiyong.tingxie.ui.group.GroupActivity
import com.zhiyong.tingxie.ui.group_member.GroupMemberActivity
import com.zhiyong.tingxie.ui.main.MainActivity
import com.zhiyong.tingxie.ui.question.RemoteQuestionActivity
import com.zhiyong.tingxie.ui.share.EnumQuizRole
import com.zhiyong.tingxie.ui.word.WordActivity
import kotlinx.coroutines.runBlocking


class MyFirebaseMessagingService : FirebaseMessagingService() {

  override fun onMessageReceived(p0: RemoteMessage) {
    super.onMessageReceived(p0)
    sendNotification(p0)
  }

  private fun sendNotification(p0: RemoteMessage) {
    val intent = with(p0.data["action"]) {
      when {
        this?.equals("QUIZ_MEMBER_ADD") == true || this?.startsWith("QUIZ_ROLE_CHANGE") == true -> {
          Intent(baseContext, WordActivity::class.java).putExtra(
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
        }
        this?.equals("GROUP_MEMBER_ADD") == true -> {
          Intent(baseContext, GroupMemberActivity::class.java).putExtra(
            EXTRA_USER_ROLE,
            UserRole(
              p0.data["groupId"]?.toLong() ?: -1,
              EnumQuizRole.valueOf(p0.data["role"] ?: "")
            )
          )
        }
        this?.equals("GROUP_DELETE") == true || this?.equals("GROUP_MEMBER_REMOVE") == true -> Intent(
          baseContext,
          GroupActivity::class.java
        )
        else -> Intent(baseContext, MainActivity::class.java)
      }
    }

    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent =
      PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val channelId = "fcm_default_channel"
    val defaultSoundUri =
      RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
      .setContentTitle(p0.notification?.title)
      .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon))
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

//  private fun getIntent(p0: RemoteMessage): Intent {
//    return with(p0.data["action"]) {
//      when {
//        this?.equals("QUIZ_MEMBER_ADD") == true || this?.startsWith("QUIZ_ROLE_CHANGE") == true -> {
//          val i = Intent(baseContext, WordActivity::class.java)
//          i.putExtra(
//            RemoteQuestionActivity.EXTRA_QUIZ_ITEM,
//            NetworkQuiz(
//              p0.data["entityId"]?.toLong() ?: -1,
//              p0.data["title"] ?: "NO TITLE",
//              p0.data["date"]?.toInt() ?: -1,
//              p0.data["email"] ?: "NO EMAIL",
//              p0.data["role"] ?: "NO ROLE",
//              p0.data["numWords"]?.toInt() ?: -1,
//              p0.data["numNotCorrect"]?.toInt() ?: -1,
//              p0.data["round"]?.toInt() ?: -1
//            )
//          )
//          return i
//        }
//        this?.equals("GROUP_MEMBER_ADD") == true -> {
//          val i = Intent(baseContext, GroupMemberActivity::class.java)
//          i.putExtra(
//            EXTRA_USER_ROLE,
//            UserRole(
//              p0.data["groupId"]?.toLong() ?: -1,
//              EnumQuizRole.valueOf(p0.data["role"] ?: "")
//            )
//          )
//          return i
//        }
//        this?.equals("GROUP_DELETE") == true || this?.equals("GROUP_MEMBER_REMOVE") == true -> Intent(
//          baseContext,
//          GroupActivity::class.java
//        )
//        else -> Intent(baseContext, MainActivity::class.java)
//      }
//    }
//  }

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