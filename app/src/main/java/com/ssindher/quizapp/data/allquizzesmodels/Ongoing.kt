package com.ssindher.quizapp.data.allquizzesmodels

data class Ongoing(
    val deletedAt: Any?,
    val quizDetails: List<QuizDetailX>,
    val scheduleEnd: String,
    val scheduleStart: String
)