package com.randy.plantapp.ui.profile.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.randy.plantapp.model.User
import com.randy.plantapp.repository.PlantRepository
import com.randy.plantapp.response.AvatarResponse
import com.randy.plantapp.utils.UiState
import com.randy.plantapp.utils.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val plantRepository: PlantRepository,
) : ViewModel() {

    private val _user = MutableLiveData<UiState<User>>()
    val user: LiveData<UiState<User>> = _user

    private val _listAvatar = MutableLiveData<UiState<List<AvatarResponse>>>()
    val listAvatar: LiveData<UiState<List<AvatarResponse>>> = _listAvatar

    private val _message = MutableLiveData<UiState<String>>()
    val message: LiveData<UiState<String>> = _message

    init {
        getAvatars()
    }

    private fun getAvatars() = viewModelScope.launch {
        _listAvatar.value = UiState.Loading
        plantRepository.getAvatars().collect { result ->
            _listAvatar.value = when (result) {
                is Result.Error -> UiState.Error(result.exception)
                is Result.Success -> {
                    UiState.Success(result.data)
                }
            }
        }
    }

    fun getUserById(userId: Int) = viewModelScope.launch {
        _user.value = UiState.Loading
        plantRepository.getUserId(userId).collect { result ->
            _user.value = when (result) {
                is Result.Error -> UiState.Error(result.exception)
                is Result.Success -> {
                    UiState.Success(result.data)
                }
            }
        }
    }

    fun updateUser(
        user: User
    ) = viewModelScope.launch {
        _message.value = UiState.Loading
        plantRepository.updateUser(user).collect{ result ->
            _message.value =  when (result) {
                is Result.Error -> UiState.Error(result.exception)
                is Result.Success -> {
                    UiState.Success(result.data.message)
                }
            }
        }
    }

    fun updateAvatarUser(
        id:Int,
        avatarUrl: String,
    ) = viewModelScope.launch {
        _message.value = UiState.Loading
        plantRepository.updateAvatarUser(id, avatarUrl).collect{ result ->
            _message.value =  when (result) {
                is Result.Error -> UiState.Error(result.exception)
                is Result.Success -> {
                    UiState.Success(result.data.message)
                }
            }
        }
    }

}