package com.randy.plantapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.randy.plantapp.model.Plant
import com.randy.plantapp.model.User
import com.randy.plantapp.repository.PlantRepository
import com.randy.plantapp.utils.UiState
import com.randy.plantapp.utils.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val plantRepository: PlantRepository,
) : ViewModel() {

    private val _user = MutableLiveData<UiState<User>>()
    val user: LiveData<UiState<User>> = _user

    private val _lisPlant = MutableLiveData<UiState<List<Plant>>>()
    val listPlant: LiveData<UiState<List<Plant>>> = _lisPlant

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

   fun getPlants(userId: Int) = viewModelScope.launch {
        _lisPlant.value = UiState.Loading
        plantRepository.getPlants(userId).collect { result ->
            _lisPlant.value = when (result) {
                is Result.Error -> UiState.Error(result.exception)
                is Result.Success -> {
                    UiState.Success(result.data)
                }
            }
        }
    }

}