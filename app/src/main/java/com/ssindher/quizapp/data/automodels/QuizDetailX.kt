package com.ssindher.quizapp.data.automodels

data class QuizDetailX(
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
    val settings: SettingsXX,
    val updated_at: String
)