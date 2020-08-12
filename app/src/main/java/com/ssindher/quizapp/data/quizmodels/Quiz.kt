package com.ssindher.quizapp.data.quizmodels

data class Quiz(
    val course: String,
    val courseId: String,
    val duration: Int,
    val instructions: List<String>,
    val isSuffle: Boolean,
    val name: String,
    val orgId: String,
    val `public`: Boolean,
    val published: Boolean,
    val quizType: Int
)