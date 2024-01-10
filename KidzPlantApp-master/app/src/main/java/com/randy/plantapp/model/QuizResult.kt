package com.randy.plantapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizResult(
    val id: Int,
    val title: String,
    val iconUrl: String,
    val scores: List<Score>
): Parcelable

@Parcelize
data class Score (
    val attempt: Int,
    val score: Int,
): Parcelable