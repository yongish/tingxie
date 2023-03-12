package com.zhiyong.tingxie.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import java.text.SimpleDateFormat
import java.util.*

object Util {
  val DB_FORMAT = SimpleDateFormat("yyyyMMdd", Locale.US)
  val DISPLAY_FORMAT = SimpleDateFormat("dd MMM yyyy", Locale.US)

  fun emailZhiyong(activity: FragmentActivity) {
    val i = Intent(Intent.ACTION_SEND)
    i.type = "message/rfc822"
    i.putExtra(Intent.EXTRA_EMAIL, arrayOf("yongish@gmail.com"))
    i.putExtra(Intent.EXTRA_SUBJECT, "Question on 听写")
    i.putExtra(Intent.EXTRA_TEXT, "Feel free to provide your feedback or ask questions in English or Chinese. 您可以用中文或英文提问。")
    try {
      activity.startActivity(Intent.createChooser(i, "Email Zhiyong."))
    } catch (ex: ActivityNotFoundException) {
      Toast.makeText(
          activity,"There are no email clients installed.", Toast.LENGTH_SHORT
      ).show()
    }
  }

  fun isOnline(context: Context): Boolean {
    val connectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
      val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
      if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
          Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
          return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
          Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
          return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
          Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
          return true
        }
      }
    }
    return false
  }
}
