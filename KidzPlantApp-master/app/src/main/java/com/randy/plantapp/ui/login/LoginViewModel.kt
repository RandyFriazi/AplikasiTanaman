package com.randy.plantapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.randy.plantapp.model.User
import com.randy.plantapp.repository.PlantRepository
import com.randy.plantapp.utils.UiState
import com.randy.plantapp.utils.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val plantRepository: PlantRepository,
) : ViewModel() {

    private val _user = MutableLiveData<UiState<User>>()
    val user: LiveData<UiState<User>> = _user

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        _user.value = UiState.Loading
        plantRepository.loginUser(email, password).collect{ result ->
            _user.value = when(result){
                is Result.Error -> UiState.Error(result.exception)
                is Result.Success -> {
                    UiState.Success(result.data)
                }
            }
        }

    }

}