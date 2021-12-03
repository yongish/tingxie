package com.zhiyong.tingxie

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
  override fun onMessageReceived(p0: RemoteMessage) {
    super.onMessageReceived(p0)

    // Open activity for list of friend requests.
//    Handler(Looper.getMainLooper()).post {
//      // todo: Get friend name and email address.
//      Toast.makeText(getApplicationContext(), "New friend request from <NAME>(<EMAIL@email.com>).", Toast.LENGTH_SHORT).show()
//    }

    // Open the quiz activity.

    Handler(Looper.getMainLooper()).post {
      Toast.makeText(getApplicationContext(), "Quiz shared with you.", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onNewToken(p0: String) {
    super.onNewToken(p0)

    // todo: Send to server.
    Log.d("NEW TOKEN", p0);
  }
}