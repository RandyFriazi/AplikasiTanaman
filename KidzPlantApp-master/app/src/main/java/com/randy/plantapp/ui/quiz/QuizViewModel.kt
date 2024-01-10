package com.randy.plantapp.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.randy.plantapp.model.Quiz
import com.randy.plantapp.model.QuizResult
import com.randy.plantapp.repository.PlantRepository
import com.randy.plantapp.utils.UiState
import com.randy.plantapp.utils.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val plantRepository: PlantRepository,
) : ViewModel() {

    private val _listQuiz = MutableLiveData<UiState<List<Quiz>>>()
    val listQuiz: LiveData<UiState<List<Quiz>>> = _listQuiz

    private val _listQuizResult = MutableLiveData<UiState<List<QuizResult>>>()
    val listQuizResult: LiveData<UiState<List<QuizResult>>> = _listQuizResult

    private val _message = MutableLiveData<UiState<String>>()
    val message: LiveData<UiState<String>> = _message

    fun getQuizByPlantId(plantId: Int) = viewModelScope.launch {
        _listQuiz.value = UiState.Loading
        plantRepository.getQuizByPlantId(plantId).collect{ result ->
            _listQuiz.value = when (result) {
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

    fun setQuizScore(
        userId: Int,
        plantId: Int,
        score: Int,
    ) = viewModelScope.launch {
        _message.value = UiState.Loading
        plantRepository.setQuizScore(userId, plantId, score).collect{ result ->
            _message.value =  when (result) {
                is Result.Error -> UiState.Error(result.exception)
                is Result.Success -> {
                    UiState.Success(result.data.message)
                }
            }
        }
    }

}