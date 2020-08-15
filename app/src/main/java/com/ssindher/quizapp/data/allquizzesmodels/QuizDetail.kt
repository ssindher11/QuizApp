package com.ssindher.quizapp.data.allquizzesmodels

data class QuizDetail(
    val __v: Int,
    val _id: String,
    val courseId: String,
    val createdBy: String,
    val created_at: String,
    val deletedAt: Any?,
    val duration: Int,
    val instructions: List<String>,
    val name: String,
    val orgId: String,
    val published: Boolean,
    val quizMarks: Int,
    val quizType: Int,
    val settings: SettingsX,
    val updated_at: String
)