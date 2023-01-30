package com.zhiyong.tingxie.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
  // TODO: Implement the ViewModel

  // stopped here
  private val _profile = MutableLiveData<Profile>()
  val profile: LiveData<Profile> = _profile
}