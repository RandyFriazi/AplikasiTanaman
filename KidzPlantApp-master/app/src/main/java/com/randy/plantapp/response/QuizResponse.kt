package com.randy.plantapp.response

import com.google.gson.annotations.SerializedName
import com.randy.plantapp.model.AnswerOptions
import com.randy.plantapp.model.Quiz

//data class QuizResponse(
//
//	@field:SerializedName("QuizResponse")
//	val quizResponse: List<QuizResponseItem>
//)

data class QuizResponseItem(
	@field:SerializedName("id")
	val id: Int,
	@field:SerializedName("plantId")
	val plantId: Int,
	@field:SerializedName("quizType")
	val quizType: Int,
	@field:SerializedName("imageQuestionUrl")
	val imageQuestionUrl: String?,
	@field:SerializedName("textQuestion")
	val textQuestion: String,
	@field:SerializedName("correctAnswer")
	val correctAnswer: Int,
	@field:SerializedName("answers")
	val answers: Answers,
)

data class Answers(
	@field:SerializedName("textAnswer1")
	val textAnswer1: String?,
	@field:SerializedName("textAnswer2")
	val textAnswer2: String?,
	@field:SerializedName("textAnswer3")
	val textAnswer3: String?,
	@field:SerializedName("textAnswer4")
	val textAnswer4: String?,
	@field:SerializedName("imageAnswer1")
	val imageAnswer1: String?,
	@field:SerializedName("imageAnswer2")
	val imageAnswer2: String?,
	@field:SerializedName("imageAnswer3")
	val imageAnswer3: String?,
	@field:SerializedName("imageAnswer4")
	val imageAnswer4: String?,
)

fun QuizResponseItem.asExternalModel() = Quiz(
	id = id,
	plantId = plantId,
	questionType = quizType,
	imageQuestionUrl = imageQuestionUrl.orEmpty(),
	textQuestion = textQuestion,
	correctAnswer = correctAnswer,
	tempAnswer = 0,
	answerOptions = AnswerOptions(
		textOption1 = answers.textAnswer1.orEmpty(),
		textOption2 = answers.textAnswer2.orEmpty(),
		textOption3 = answers.textAnswer3.orEmpty(),
		textOption4 = answers.textAnswer4.orEmpty(),
		imageOption1 = answers.imageAnswer1.orEmpty(),
		imageOption2 = answers.imageAnswer2.orEmpty(),
		imageOption3 = answers.imageAnswer3.orEmpty(),
		imageOption4 = answers.imageAnswer4.orEmpty()
	)

)

