package com.ssindher.quizapp.data.model

data class Batch(
    val ongoing: List<Quiz>,
    val upcoming: List<Quiz>,
    val completed: List<Quiz>
)