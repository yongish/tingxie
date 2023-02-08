package com.zhiyong.tingxie.ui.readings

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.zhiyong.tingxie.R

class CharacterView : ConstraintLayout {
  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet?) : super(
    context,
    attrs
  )

  constructor(
    context: Context, attrs: AttributeSet?,
    @AttrRes defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr)

  constructor(context: Context, character: String) : super(context) {
    init(character)
  }

  fun init(character: String) {
    inflate(context, R.layout.fragment_character, this)
    val textView = findViewById<TextView>(R.id.tvCharacter)
    textView.text = character
  }
}