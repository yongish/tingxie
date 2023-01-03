package com.zhiyong.tingxie.ui.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zhiyong.tingxie.FakeQuizRepository
import com.zhiyong.tingxie.network.NetworkQuiz
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuizViewModelTest {
  private lateinit var quizViewModel: QuizViewModel

  @Before
  fun setupViewModel() {
    quizViewModel = QuizViewModel(
      FakeQuizRepository(
        mutableListOf(
          NetworkQuiz(
            0,
            "title0",
            20230101,
            "email0",
            "role0",
            0,
            0,
            1
          ),
          NetworkQuiz(
            1,
            "title1",
            20230101,
            "email1",
            "role0",
            0,
            0,
            1
          )
        )
      )
    )
  }

//  subjectUnderTest_actionOrInput_resultState
  @Test
  fun getQuizzes_initial_returns2quizzes() {
    quizViewModel.createQuiz("title0", 20230101)
    assertThat(quizViewModel.allQuizItems.value!!.size, `is`(2))
  }
}
