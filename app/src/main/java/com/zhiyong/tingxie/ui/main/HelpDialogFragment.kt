package com.zhiyong.tingxie.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.DialogFragment
import com.zhiyong.tingxie.databinding.FragmentHelpBinding
import com.zhiyong.tingxie.ui.Util

class HelpDialogFragment : DialogFragment() {
  private var _binding: FragmentHelpBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    _binding = FragmentHelpBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.btnPlayDemo.setOnClickListener { startActivity(
        Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/xqOFlM8_vak"))
    ) }
    binding.btnEmailZhiyong.setOnClickListener { Util.emailZhiyong(requireActivity()) }
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