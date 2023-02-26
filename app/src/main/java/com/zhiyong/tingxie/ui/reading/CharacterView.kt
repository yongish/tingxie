package com.zhiyong.tingxie.ui.reading

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.zhiyong.tingxie.R
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType

enum class Position {
  FIRST, INSIDE, LAST
}

class CharacterView : ConstraintLayout {
  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  constructor(
    context: Context,
    attrs: AttributeSet?,
    @AttrRes defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr)

  private lateinit var pinyinView: TextView
  var groupIndex = 0
  var position = "FIRST"

    constructor(context: Context, character: String, groupIndex: Int, position: Position) : super(context) {
    inflate(context, R.layout.fragment_character, this)

    // Need group index to highlight/lookup entire word on click.
    this.groupIndex = groupIndex

    val textView = findViewById<TextView>(R.id.tvCharacter)
    textView.text = character

    val format = HanyuPinyinOutputFormat()
    format.vCharType = HanyuPinyinVCharType.WITH_U_UNICODE
    format.toneType = HanyuPinyinToneType.WITH_TONE_MARK
    pinyinView = findViewById(R.id.tvPinyin)
    pinyinView.text = PinyinHelper.toHanYuPinyinString(character, format, " ", true)

    when (position) {
      Position.FIRST -> pinyinView.setBackgroundResource(R.drawable.character_first)
      Position.INSIDE -> pinyinView.setBackgroundResource(R.drawable.character_middle)
      else -> pinyinView.setBackgroundResource(R.drawable.character_last)
    }

    // How to underline pinyinView?
    // How to partially underline pinyinView?

  }

  fun togglePinyin() {
    pinyinView.visibility = if (pinyinView.isVisible) View.INVISIBLE else View.VISIBLE
  }

  fun toggleUnderline() {

  }

  override fun setOnClickListener(l: OnClickListener?) {
    super.setOnClickListener(l)

    // Dialog to see the definition of a word.
    // 1. Choose a future quiz to add a word to.
    // 2. Create a new quiz that contains this word.
  }
}