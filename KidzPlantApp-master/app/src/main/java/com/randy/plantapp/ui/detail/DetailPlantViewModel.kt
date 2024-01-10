package com.randy.plantapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.randy.plantapp.model.DetailPlant
import com.randy.plantapp.repository.PlantRepository
import com.randy.plantapp.utils.UiState
import com.randy.plantapp.utils.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPlantViewModel @Inject constructor(
    private val plantRepository: PlantRepository,
) : ViewModel() {

    private val _detailPlant = MutableLiveData<UiState<DetailPlant>>()
    val detailPlant: LiveData<UiState<DetailPlant>> = _detailPlant

    private val _message = MutableLiveData<UiState<String>>()
    val message: LiveData<UiState<String>> = _message

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

    fun setProgressMateriBySubPlantId(
        userId: Int,
        plantId: Int,
        subPlantId: Int,
        isCompleted: Boolean
    ) = viewModelScope.launch {
        _message.value = UiState.Loading
        plantRepository.setProgressMateriBySubPlantId(userId, plantId, subPlantId, isCompleted).collect{ result ->
            _message.value = when (result) {
                is Result.Error -> UiState.Error(result.exception)
                is Result.Success -> {
                    UiState.Success(result.data.message)
                }
            }
        }
    }



//    private val _detailPlant = MutableLiveData<UiState<DetailPlant>>()
//    val detailPlant: LiveData<UiState<DetailPlant>> = _detailPlant

//    fun getDetailPlantById(plantId: Int) = viewModelScope.launch {
//        _detailPlant.value = UiState.Loading
//        plantRepository.getDetailPlantById(plantId).collect{ result ->
//            _detailPlant.value = when (result) {
//                is Result.Error -> UiState.Error(result.exception)
//                is Result.Success -> {
//                    UiState.Success(result.data)
//                }
//            }
//        }
//    }

}