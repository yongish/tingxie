package com.zhiyong.tingxie.ui.hsk.words

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.zhiyong.tingxie.QuizRepository

class HskWordsViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  fun getHsk(level: Int): List<HskWordsAdapter.HskWord> = repository.getHsk(level)

  fun getUnaskedHskWords(level: Int): List<HskWordsAdapter.HskWord> = repository.getUnaskedHskWords(level)

  fun getHanzis(level: Int, pinyin: String): List<String> = repository.getHanzis(level, pinyin)

  fun resetAsked(level: Int): Boolean = repository.resetAsked(level)

  fun setAsked(index: Int) = repository.setAsked(index)
}
