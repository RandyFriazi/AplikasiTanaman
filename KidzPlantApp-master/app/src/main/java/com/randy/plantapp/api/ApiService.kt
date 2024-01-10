package com.randy.plantapp.api

import com.randy.plantapp.response.BasePlantResponse
import com.randy.plantapp.response.PlantResponse
import com.randy.plantapp.response.QuizResponseItem
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String,
    ): BasePlantResponse

    @FormUrlEncoded
    @POST("signup.php")
    suspend fun signUpUser(
        @Field("username") username:String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): BasePlantResponse

    @GET("user.php")
    suspend fun getUserById(
        @Query("id") userId: Int
    ): BasePlantResponse

    @FormUrlEncoded
    @POST("user.php")
    suspend fun updateUser(
        @Field("id") id:Int,
        @Field("username") username:String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("avatarUrl") avatarUrl: String,
    ): BasePlantResponse

    @FormUrlEncoded
    @POST("user.php")
    suspend fun updateAvatar(
        @Field("id") id:Int,
        @Field("avatarUrl") avatarUrl: String,
    ): BasePlantResponse

    @GET("avatar.php")
    suspend fun getAvatars(): BasePlantResponse

    @GET("plant.php")
    suspend fun getPlants(
        @Query("userId") userId: Int,
    ): List<PlantResponse>

    @GET("subPlant.php")
    suspend fun getSubPlantsById(
        @Query("userId") userId: Int,
        @Query("plantId") plantId: Int
    ): PlantResponse

    @FormUrlEncoded
    @POST("subPlant.php")
    suspend fun setProgressMateriBySubPlantId(
        @Field("userId") userId: Int,
        @Field("plantId") plantId: Int,
        @Field("subPlantId") subPlantId: Int,
        @Field("isCompleted") isCompleted: Boolean
    ): BasePlantResponse

    @GET("quiz.php")
    suspend fun getQuizByPlantId(
        @Query("plantId") plantId: Int
    ): List<QuizResponseItem>

    @GET("quizResult.php")
    suspend fun getQuizResultByUserId(
        @Query("userId") userId: Int
    ): List<PlantResponse>

    @FormUrlEncoded
    @POST("quizResult.php")
    suspend fun setQuizScore(
        @Field("userId") userId: Int,
        @Field("plantId") plantId: Int,
        @Field("score") score: Int,
    ): BasePlantResponse

}