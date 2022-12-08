package com.zhiyong.tingxie.ui.share

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//class ShareIndividualViewModelFactory(
//    private val quizId: Long,private val application: Application
//    ) : ViewModelProvider.Factory {
//  override fun <T : ViewModel> create(modelClass: Class<T>): T {
//    if (modelClass.isAssignableFrom(ShareIndividualViewModel::class.java)) {
//      return ShareIndividualViewModel(quizId, application) as T
//    }
//    throw IllegalArgumentException("Unknown ViewModel class")
//  }
//}