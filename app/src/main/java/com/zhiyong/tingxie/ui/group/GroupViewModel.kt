package com.zhiyong.tingxie.ui.group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkGroup
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuizRepository(application)

    private val _groupsStatus = MutableLiveData<Status>()
    val groupsStatus: LiveData<Status> = _groupsStatus

    private val _groups = MutableLiveData<List<NetworkGroup>>()
    val groups: LiveData<List<NetworkGroup>> = _groups

    init {
        getGroups()
    }

    private fun getGroups() {
        viewModelScope.launch {
            _groupsStatus.value = Status.LOADING
            try {
                _groups.value = repository.getGroups()
                _groupsStatus.value = Status.DONE
            } catch (e: Exception) {
                _groupsStatus.value = Status.ERROR
                _groups.value = listOf()
            }
        }
    }

    fun createGroup(groupName: String, members: List<NetworkGroupMember>): LiveData<Long> {
        val result = MutableLiveData<Long>()
        viewModelScope.launch(Dispatchers.IO) {
            val newId = repository.createGroup(groupName, members)
            result.postValue(newId)
        }
        return result
    }
}
