package com.randy.plantapp.model

data class Quiz(
    val id: Int,
    val plantId: Int,
    val questionType: Int,
    val imageQuestionUrl: String,
    val textQuestion: String,
    val correctAnswer: Int,
    var tempAnswer: Int,
    val answerOptions: AnswerOptions,
)

data class AnswerOptions(
    val textOption1: String,
    val textOption2: String,
    val textOption3: String,
    val textOption4: String,
    val imageOption1: String,
    val imageOption2: String,
    val imageOption3: String,
    val imageOption4: String,
)
