package com.example.music.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationItemsViewModel :ViewModel(){
    private val _updateItem = MutableLiveData<Int>()

    val updateItem :LiveData<Int>
        get() = _updateItem

    fun changeNotificationItemStates(item:Int){
        _updateItem.value = item
    }
}