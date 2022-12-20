package com.zhiyong.tingxie.ui.group_member

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentSelectRoleBinding

private const val POSITION = "position"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectRoleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectRoleFragment : DialogFragment() {

  // TODO: Rename and change types of parameters
  private var position: Int? = null

  private var _binding: FragmentSelectRoleBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSelectRoleBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    position = arguments?.getInt("position")

    binding.btnOk.setOnClickListener {
      val role = if (binding.radioGroup.checkedRadioButtonId == R.id.rbViewer) "MEMBER" else "ADMIN"
      setFragmentResult("requestKey", bundleOf("position" to position, "role" to role))
      dismiss()
    }

    binding.btnCancel.setOnClickListener {
      dismiss()
    }
  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectRoleFragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(position: Int) =
      SelectRoleFragment().apply {
        arguments = Bundle().apply {
          putInt(POSITION, position)
        }
      }
  }
}