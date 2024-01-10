package com.randy.plantapp.ui.progress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.randy.plantapp.model.DetailPlant
import com.randy.plantapp.model.Plant
import com.randy.plantapp.model.QuizResult
import com.randy.plantapp.repository.PlantRepository
import com.randy.plantapp.utils.UiState
import com.randy.plantapp.utils.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val plantRepository: PlantRepository,
) : ViewModel() {

    private val _listQuizResult = MutableLiveData<UiState<List<QuizResult>>>()
    val listQuizResult: LiveData<UiState<List<QuizResult>>> = _listQuizResult

    private val _lisPlant = MutableLiveData<UiState<List<Plant>>>()
    val listPlant: LiveData<UiState<List<Plant>>> = _lisPlant

    private val _detailPlant = MutableLiveData<UiState<DetailPlant>>()
    val detailPlant: LiveData<UiState<DetailPlant>> = _detailPlant

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

    fun getSubPlantsById(userId: Int, plantId: Int) = viewModelScope.launch {
        _detailPlant.value = UiState.Loading
        plantRepository.getSubPlantsById(userId, plantId).collect { result ->
            _detailPlant.value = when (result) {
                is Result.Error -> UiState.Error(result.exception)
                is Result.Success -> {
                    UiState.Success(result.data)
                }
            }
        }
    }

    fun getQuizResultByUserId(userId: Int) = viewModelScope.launch {
        _listQuizResult.value = UiState.Loading
        plantRepository.getQuizResultByUserId(userId).collect{ result ->
            _listQuizResult.value = when (result) {
                is Result.Error -> UiState.Error(result.exception)
                is Result.Success -> {
                    UiState.Success(result.data)
                }
            }
        }
    }



}