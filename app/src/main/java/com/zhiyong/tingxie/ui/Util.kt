package com.zhiyong.tingxie.ui

import android.content.ActivityNotFoundException
import android.content.Intent
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
}
