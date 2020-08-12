package com.ssindher.quizapp.data.automodels

data class Completed(
    val deletedAt: Any?,
    val quizDetails: List<QuizDetail>,
    val scheduleEnd: String,
    val scheduleStart: String
)