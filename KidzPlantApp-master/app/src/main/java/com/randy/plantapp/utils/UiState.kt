package com.randy.plantapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.Flow
import com.randy.plantapp.utils.result.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>
    data class Error<T>(val exception: Throwable?) : UiState<T>
    object Loading : UiState<Nothing>
}

fun <T> Flow<Result<T>>.asUiState(): LiveData<UiState<T>> = liveData {
    emit(UiState.Loading)
    this@asUiState.collect { result ->
        when (result) {
            is Result.Error -> emit(UiState.Error(exception = result.exception))
            is Result.Success -> emit(UiState.Success(data = result.data))
        }

    }
}

fun <T> Flow<T>.asUiState(): Flow<UiState<T>> = this
    .map<T, UiState<T>> {
        UiState.Success(it)
    }
    .onStart { emit(UiState.Loading) }
    .catch {
        emit(UiState.Error(it))
    }