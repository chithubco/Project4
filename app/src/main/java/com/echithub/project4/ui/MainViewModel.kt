package com.echithub.project4.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application):AndroidViewModel(application) {
    val downloadState = MutableLiveData<Boolean>()

    init {
        downloadState.value = false
    }

    override fun onCleared() {
        super.onCleared()
    }
}