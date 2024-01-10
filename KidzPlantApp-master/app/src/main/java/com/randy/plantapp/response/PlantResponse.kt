package com.randy.plantapp.response

import com.google.gson.annotations.SerializedName
import com.randy.plantapp.model.*

data class PlantResponse(
	@field:SerializedName("id")
	val id: Int,
	@field:SerializedName("title")
	val title: String,
	@field:SerializedName("iconUrl")
	val iconUrl: String,
	@field:SerializedName("description")
	val description: String,
	@field:SerializedName("videoUrl")
	val videoUrl: String,
	@field:SerializedName("countCompleted")
	val countCompleted: Int,
	@field:SerializedName("videoIsCompleted")
	val videoIsCompleted: Boolean,
	@field:SerializedName("listSubPlant")
	val listSubPlant: List<SubPlantResponse>,
	@field:SerializedName("scores")
	val scores: List<ScoreResponse>
)

data class SubPlantResponse(
	@field:SerializedName("id")
	val id: Int,
	@field:SerializedName("plantId")
	val plantId: Int,
	@field:SerializedName("subPlantId")
	val subPlantId: Int,
	@field:SerializedName("title")
	val title: String,
	@field:SerializedName("iconUrl")
	val iconUrl: String,
	@field:SerializedName("imageUrl1")
	val imageUrl1: String?,
	@field:SerializedName("imageUrl2")
	val imageUrl2: String?,
	@field:SerializedName("imageUrl3")
	val imageUrl3: String?,
	@field:SerializedName("videoUrl")
	val videoUrl: String,
	@field:SerializedName("audioUrl")
	val audioUrl: String,
	@field:SerializedName("benefit")
	val benefit: String,
	@field:SerializedName("isCompleted")
	val isCompleted: Boolean,
)

data class ScoreResponse (
	@field:SerializedName("attempt")
	val attempt: Int?,
	@field:SerializedName("score")
	val score: Int?,
)

fun PlantResponse.asExternalModel() = Plant(
	id = id,
	title = title,
	iconUrl = iconUrl,
	desc = description,
	videoUrl = videoUrl,
	countCompleted = countCompleted
)

//fun SubPlantResponse.asExternalModel() = SubPlant(
//	id = id,
//	title = title,
//	iconUrl = iconUrl,
//	imageUrl1 = imageUrl1.orEmpty(),
//	imageUrl2 = imageUrl2.orEmpty(),
//	imageUrl3 = imageUrl3.orEmpty(),
//	videoUrl = videoUrl,
//	benefit = benefit
//)

fun PlantResponse.asExternalDetailModel() = DetailPlant(
	videoIsCompleted = videoIsCompleted,
	subPlants = listSubPlant.map { subPlant ->
		SubPlant(
			id = subPlant.id,
			plantId = subPlant.plantId,
			subPlantId = subPlant.subPlantId,
			title = subPlant.title,
			iconUrl = subPlant.iconUrl,
			imageUrl1 = subPlant.imageUrl1.orEmpty(),
			imageUrl2 = subPlant.imageUrl2.orEmpty(),
			imageUrl3 = subPlant.imageUrl3.orEmpty(),
			videoUrl = subPlant.videoUrl,
			audioUrl = subPlant.audioUrl,
			benefit = subPlant.benefit,
			isCompleted = subPlant.isCompleted
		)
	}
)

fun PlantResponse.asExternalQuizResultModel() = QuizResult(
	id = id,
	title = title,
	iconUrl = iconUrl,
	scores = scores.map { Score(it.attempt?:0, it.score?:0) }
)