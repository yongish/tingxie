package com.zhiyong.tingxie

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
  CoroutineWorker(appContext, params) {
  companion object {
    const val WORK_NAME = "com.zhiyong.tingxie.RefreshDataWorker"
  }

  override suspend fun doWork(): Result {
    val repository =  QuizRepository(applicationContext)

    try {
      repository.refreshQuizzes()
    } catch (e: HttpException) {
      return Result.retry()
    }

    return Result.success()
  }
}
