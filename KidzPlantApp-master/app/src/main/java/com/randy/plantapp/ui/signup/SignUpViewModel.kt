package com.randy.plantapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.randy.plantapp.model.User
import com.randy.plantapp.repository.PlantRepository
import com.randy.plantapp.response.BasePlantResponse
import com.randy.plantapp.utils.UiState
import com.randy.plantapp.utils.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val plantRepository: PlantRepository,
) : ViewModel() {

    private val _message = MutableLiveData<UiState<String>>()
    val message: LiveData<UiState<String>> = _message

    private val _response = MutableLiveData<UiState<BasePlantResponse>>()
    val response: LiveData<UiState<BasePlantResponse>> = _response

    fun registerUser(
        username:String,
        email: String,
        password: String,
    ) = viewModelScope.launch {
        _response.value = UiState.Loading
        plantRepository.registerUser(username, email, password).collect{ result ->
            _response.value =  when (result) {
                is Result.Error -> UiState.Error(result.exception)
                is Result.Success -> {
                    UiState.Success(result.data)
                }
            }

        }
    }

}