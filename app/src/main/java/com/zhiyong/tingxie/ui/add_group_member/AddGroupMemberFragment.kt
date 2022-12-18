package com.zhiyong.tingxie.ui.add_group_member

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.R

class AddGroupMemberFragment : Fragment() {

  companion object {
    fun newInstance() = AddGroupMemberFragment()
  }

  private lateinit var viewModel: AddGroupMemberViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(this).get(AddGroupMemberViewModel::class.java)
    // TODO: Use the ViewModel
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.fragment_add_group_member, container, false)
  }

}