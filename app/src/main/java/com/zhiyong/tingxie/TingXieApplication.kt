package com.zhiyong.tingxie

import android.app.Application
import timber.log.Timber

class TingXieApplication : Application() {
  val quizRepository: Repository
    get() = ServiceLocator.provideQuizRepository(this)

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
  }
}

//import android.app.Application
//import androidx.work.*
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import java.util.concurrent.TimeUnit
//
//class TingXieApplication : Application() {
//  private val applicationScope = CoroutineScope(Dispatchers.Default)
//
//  override fun onCreate() {
//    super.onCreate()
//    delayedInit()
//  }
//
//  private fun delayedInit() {
//    applicationScope.launch {
//      setupRecurringWork()
//    }
//  }
//
//  private fun setupRecurringWork() {
//    val constraints = Constraints.Builder()
//      .setRequiredNetworkType(NetworkType.CONNECTED)
//      .setRequiresBatteryNotLow(true)
//      .build()
//
//    val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
//      .setConstraints(constraints)
//      .build()
//
//    WorkManager.getInstance().enqueueUniquePeriodicWork(
//      RefreshDataWorker.WORK_NAME,
//      ExistingPeriodicWorkPolicy.KEEP,
//      repeatingRequest)
//  }
//}