package com.example.music.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NavigationViewModel():ViewModel() {
    private var  _showState = MutableLiveData<Boolean>(true)

    val showState :LiveData<Boolean>
        get() = _showState

    fun stateVisibility(visible:Boolean){
        _showState.value = visible
    }
}