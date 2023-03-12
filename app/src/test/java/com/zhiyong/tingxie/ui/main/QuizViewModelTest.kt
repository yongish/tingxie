package com.zhiyong.tingxie.ui.main

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuizViewModelTest {
  private lateinit var quizViewModel: QuizViewModel

  @Before
  fun setupViewModel() {
    quizViewModel = QuizViewModel(ApplicationProvider.getApplicationContext())
  }

  @Test
  fun createQuiz_draft() {
//    quizViewModel.
//    assertThat()
  }
}
