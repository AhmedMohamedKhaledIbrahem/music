package com.example.music.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NavigationBottomBarViewModel():ViewModel() {
    private val _showBottomBar = MutableLiveData(true)
    val showBottomBar :LiveData<Boolean> get() = _showBottomBar

    fun setVisibilityBottomBar(visible:Boolean){
        _showBottomBar.value = visible
    }
}