package com.ssindher.quizapp.data.model

data class Quiz(
    val deletedAt: String?,
    val quizDetails: List<QuizDetails>,
    val scheduleEnd: String,
    val scheduleStart: String
)