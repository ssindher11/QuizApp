package com.ssindher.quizapp.data.model

data class QuizInfo(
    val name: String,
    val orgId: String,
    val course: String,
    val public: Boolean,
    val courseId: String,
    val duration: Int,
    val isSuffle: Boolean,
    val quizType: Int,
    val published: Boolean,
    val instructions: List<String>
)