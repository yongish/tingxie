package com.zhiyong.tingxie.ui.share

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ShareFragmentBinding
import com.zhiyong.tingxie.ui.friend.Status
import com.zhiyong.tingxie.ui.share.ShareActivity.Companion.EXTRA_QUIZ_ID

class ShareFragment : Fragment() {

  companion object {
    fun newInstance() = ShareFragment()
  }

  private lateinit var viewModel: ShareViewModel
  private lateinit var adapter: ShareAdapter
  private var _binding: ShareFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    _binding = ShareFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val quizId = arguments?.getLong(EXTRA_QUIZ_ID)

    val spannableString = SpannableString(resources.getString(R.string.no_shares))
    val d: Drawable? = ResourcesCompat.getDrawable(
        resources, R.drawable.ic_baseline_edit_white_24, null
    )
    d?.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
    spannableString.setSpan(d?.let {
      ImageSpan(it, ImageSpan.ALIGN_BOTTOM) },
        spannableString.toString().indexOf("+"),
        spannableString.toString().indexOf("+") + 1,
        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )
    binding.emptyView.text = spannableString

    binding.fab.setOnClickListener {
      // Display all friends.
      adapter.filter.filter(IsShared.ALL.name)
      binding.fab.visibility = View.GONE

      // Display done check button on menu bar.
    }

    if (quizId != null) {
      val viewModelFactory = ShareViewModelFactory(quizId, requireNotNull(activity).application)
      viewModel = ViewModelProvider(this, viewModelFactory)[ShareViewModel::class.java]

      viewModel.shares.observe(viewLifecycleOwner, { shares ->
        shares?.apply {
          val role = shares.first { it.email == FirebaseAuth.getInstance().currentUser?.email }.role
          binding.fab.visibility = if (role == EnumQuizRole.EDITOR) View.VISIBLE else View.GONE

          // todo: Pass role into ShareAdapter to fix duplication.
          binding.recyclerviewShares.adapter = ShareAdapter(
              quizId,
              shares,
//              shares.filter { it.isShared },
              requireActivity(),
              viewModel,
              binding.recyclerviewShares,
          )

        }
        adapter = binding.recyclerviewShares.adapter as ShareAdapter
        if (shares.isEmpty()) {
          binding.emptyView.visibility = View.VISIBLE
        } else {
          binding.emptyView.visibility = View.INVISIBLE
        }
      })

      viewModel.status.observe(viewLifecycleOwner, { status ->
        if (status.equals(Status.ERROR)) {
          // todo: Display an offline error message on the view, instead of a toast.
          Toast.makeText(activity, "Network Error on Friends", Toast.LENGTH_LONG).show()
        }
      })
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
