package com.zhiyong.tingxie.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.zhiyong.tingxie.R
import android.content.Intent
import android.content.ActivityNotFoundException
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class HelpDialogFragment : DialogFragment() {
  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? = inflater.inflate(R.layout.fragment_help, container)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val btnDemo = view.findViewById<Button>(R.id.btnPlayDemo)
    btnDemo.setOnClickListener { startActivity(
        Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/xqOFlM8_vak"))
    ) }
    val btnSendEmail = view.findViewById<Button>(R.id.btnEmailZhiyong)
    btnSendEmail.setOnClickListener {
      val i = Intent(Intent.ACTION_SEND)
      i.type = "message/rfc822"
      i.putExtra(Intent.EXTRA_EMAIL, arrayOf("yongish@gmail.com"))
      i.putExtra(Intent.EXTRA_SUBJECT, "Question on 听写")
      i.putExtra(Intent.EXTRA_TEXT, "Feel free to provide your feedback or ask questions in English or Chinese. 您可以用中文或英文提问。")
      try {
        startActivity(Intent.createChooser(i, "Email Zhiyong."))
      } catch (ex: ActivityNotFoundException) {
        Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
      }
    }
    //        btnFacebook = getView().findViewById(R.id.btnFacebook);
//        btnFacebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "https://www.facebook.com/n/?听写-295524347790755";
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//            }
//        });
  }

  companion object {
    @JvmStatic
    fun newInstance() = HelpDialogFragment()
  }
}