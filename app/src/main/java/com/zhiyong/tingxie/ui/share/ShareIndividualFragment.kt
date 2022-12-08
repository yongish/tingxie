package com.zhiyong.tingxie.ui.share

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ShareFragmentBinding
import com.zhiyong.tingxie.ui.friend.FriendActivity
import com.zhiyong.tingxie.ui.share.ShareActivity.Companion.EXTRA_QUIZ_ID
import com.zhiyong.tingxie.viewmodel.Status


class ShareIndividualFragment : Fragment() {

  companion object {
    fun newInstance() = ShareIndividualFragment()
  }

  private lateinit var viewModel: ShareIndividualViewModel
  private lateinit var shareIndividualAdapter: ShareIndividualAdapter
  private lateinit var menuItem: MenuItem
  private var _binding: ShareFragmentBinding? = null
  private val binding get() = _binding!!
  private var editing = false

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    _binding = ShareFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    setHasOptionsMenu(true)
    super.onCreate(savedInstanceState)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val quizId = requireActivity().intent.getLongExtra(EXTRA_QUIZ_ID, -1)

    val spannableString = SpannableString(resources.getString(R.string.no_shares))
    val d: Drawable? = ResourcesCompat.getDrawable(
        resources, R.drawable.ic_baseline_edit_white_24, null
    )
    d?.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
    spannableString.setSpan(
        d?.let { ImageSpan(it, ImageSpan.ALIGN_BOTTOM) },
        spannableString.toString().indexOf("+"),
        spannableString.toString().indexOf("+") + 1,
        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )
    binding.emptyView.text = spannableString

//    menuItem.setOnMenuItemClickListener {
//      viewModel.setAllShared(quizId, it.isChecked)
//      true
//    }

//    if (quizId != -1L) {
//      val viewModelFactory = ShareIndividualViewModelFactory(quizId, requireNotNull(activity).application)
//      viewModel = ViewModelProvider(this, viewModelFactory)[ShareIndividualViewModel::class.java]

//      viewModel.shares.observe(viewLifecycleOwner) { shares ->
//        if (shares.isEmpty()) {
//          binding.emptyView.visibility = View.VISIBLE
//        } else {
//          binding.emptyView.visibility = View.INVISIBLE
//        }
//
//        shares?.apply {
//          if (shares.isNotEmpty()) {
//            val role = shares.first { it.email == FirebaseAuth.getInstance().currentUser?.email }.role
//            binding.fab.visibility = if (role == EnumQuizRole.EDITOR) View.VISIBLE else View.GONE
//          }
//
//          // todo: Pass role into ShareAdapter to fix duplication.
//          binding.recyclerviewShares.adapter = ShareIndividualAdapter(
//              quizId,
//              shares,
//              requireActivity(),
//              viewModel,
//              binding.recyclerviewShares,
//          )
//
//          shareIndividualAdapter = binding.recyclerviewShares.adapter as ShareIndividualAdapter
//          binding.fab.setOnClickListener {
//
//            // todo: Only admin can edit shared.
//
//            if (editing) {
//              shareIndividualAdapter.filter.filter(IsShared.SHARED.name)
//              binding.fab.setImageResource(R.drawable.ic_baseline_edit_black_24)
//              shareIndividualAdapter.editing = false
//              menuItem.isVisible = false
//              editing = false
//            } else {
//              if (shares.isEmpty()) {
//                startActivity(Intent(context, FriendActivity::class.java))
//              }
//              // Display all friends.
//              shareIndividualAdapter.filter.filter(IsShared.ALL.name)
//              binding.fab.setImageResource(R.drawable.ic_baseline_done_24)
//              shareIndividualAdapter.editing = true
//              menuItem.isVisible = true
//              editing = true
//            }
//            // todo: Display done check button on menu bar.
//          }
//        }
//      }
//
//      viewModel.status.observe(viewLifecycleOwner) { status: Status ->
//        if (status == Status.ERROR) {
//          // todo: Display an offline error message on the view, instead of a toast.
//          Toast.makeText(activity, "Network Error on Shares", Toast.LENGTH_LONG).show()
//        }
//      }
//
//    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//    inflater.inflate(R.menu.menu_share, menu)
    menuItem = menu.add("Item1")
    menuItem.setActionView(R.layout.action_layout_checkbox)
        .setVisible(false)
        .setCheckable(true)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
