package com.randy.plantapp.repository

import com.randy.plantapp.api.ApiService
import com.randy.plantapp.di.IoDispatcher
import com.randy.plantapp.model.*
import com.randy.plantapp.response.*
import com.randy.plantapp.utils.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PlantRepository @Inject constructor(
    private val apiService: ApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {

    suspend fun loginUser(
        email: String,
        password: String,
    ): Flow<Result<User>> = flow {
        try {
            val response = apiService.loginUser(email, password)
            val data = response.user.asExternalModel()
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatcher)

    suspend fun registerUser(
        username:String,
        email: String,
        password: String,
    ): Flow<Result<BasePlantResponse>> = flow {
        try {
            val response = apiService.signUpUser(username, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatcher)

    suspend fun updateUser(
        user: User
    ): Flow<Result<BasePlantResponse>> = flow {
        try {
            val response = apiService.updateUser(
                user.id,
                user.username,
                user.email,
                user.password,
                user.avatarUrl,
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatcher)

    suspend fun updateAvatarUser(
        id:Int,
        avatarUrl: String,
    ): Flow<Result<BasePlantResponse>> = flow {
        try {
            val response = apiService.updateAvatar(id, avatarUrl)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatcher)

    fun getUserId(userId: Int): Flow<Result<User>> = flow {
        try {
            val response = apiService.getUserById(userId)
            val data = response.user.asExternalModel()
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatcher)

    fun getAvatars(): Flow<Result<List<AvatarResponse>>> = flow {
        try {
            val response = apiService.getAvatars()
            val data = response.avatars
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatcher)

    fun getPlants(userId: Int): Flow<Result<List<Plant>>> = flow {
        try {
            val response = apiService.getPlants(userId)
            val data = response.map { it.asExternalModel() }
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatcher)

    fun getSubPlantsById(userId: Int, plantId: Int): Flow<Result<DetailPlant>> = flow {
        try {
            val response = apiService.getSubPlantsById(userId, plantId)
            val data = response.asExternalDetailModel()
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatcher)

    suspend fun setProgressMateriBySubPlantId(
        userId: Int,
        plantId: Int,
        subPlantId: Int,
        isCompleted: Boolean
    ): Flow<Result<BasePlantResponse>> = flow {
        try {
            val response = apiService.setProgressMateriBySubPlantId(userId, plantId, subPlantId, isCompleted)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatcher)

    fun getQuizByPlantId(plantId: Int): Flow<Result<List<Quiz>>> = flow {
        try {
            val response = apiService.getQuizByPlantId(plantId)
            val data = response.map { it.asExternalModel() }
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getQuizResultByUserId(userId: Int): Flow<Result<List<QuizResult>>> = flow {
        try {
            val response = apiService.getQuizResultByUserId(userId)
            val data = response.map { it.asExternalQuizResultModel() }
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun setQuizScore(
        userId: Int,
        plantId: Int,
        score: Int,
    ): Flow<Result<BasePlantResponse>> = flow {
        try {
            val response = apiService.setQuizScore(userId, plantId, score)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatcher)

}