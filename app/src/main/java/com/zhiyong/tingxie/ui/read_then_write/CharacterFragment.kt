package com.zhiyong.tingxie.ui.read_then_write

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.databinding.FragmentCharacterBinding
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType

private const val CHARACTER = "character"

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterFragment : Fragment() {
  private var character: String? = null

  private var _binding: FragmentCharacterBinding? = null
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      character = it.getString(CHARACTER)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentCharacterBinding.inflate(inflater, container, false)
    return binding.root
  }

  companion object {
    @JvmStatic
    fun newInstance(character: String) =
      CharacterFragment().apply {
        arguments = Bundle().apply {
          putString(CHARACTER, character)
        }
      }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.tvCharacter.text = character

    val format = HanyuPinyinOutputFormat()
    format.vCharType = HanyuPinyinVCharType.WITH_U_UNICODE
    format.toneType = HanyuPinyinToneType.WITH_TONE_MARK
    binding.tvPinyin.text =
      PinyinHelper.toHanYuPinyinString(character + "a", format, " ", true)
  }
}