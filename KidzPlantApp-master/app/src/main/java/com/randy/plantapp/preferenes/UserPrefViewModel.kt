package com.randy.plantapp.preferenes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPrefViewModel @Inject constructor(private val pref: UserPreferences) : ViewModel() {

    fun getUserInfo(): LiveData<Triple<Int,Boolean,Boolean>> {
        return pref.getUserInfo().asLiveData()
    }

    fun saveUserInfo(userId: Int, loginState: Boolean) = viewModelScope.launch {
        pref.saveUserInfo(userId, loginState)
    }

    fun getQuizState(): LiveData<Boolean> {
        return pref.getQuizState().asLiveData()
    }

    fun saveQuizState(quizState: Boolean) = viewModelScope.launch {
        pref.saveQuizState(quizState)
    }

    fun getOnBoardState(): LiveData<Boolean> {
        return pref.getQuizState().asLiveData()
    }

    fun saveOnBoardState(onBoardState: Boolean) = viewModelScope.launch {
        pref.saveQuizState(onBoardState)
    }

    fun logout() = viewModelScope.launch {
        pref.logout()
    }

}