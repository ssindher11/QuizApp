package com.ssindher.quizapp.data.model

data class QuizDetails(
    val __v: Int,
    val _id: String,
    val name: String,
    val orgId: String,
    val courseId: String,
    val duration: Int,
    val quizType: Int,
    val settings: Settings,
    val createdBy: String,
    val deletedAt: String?,
    val published: Boolean,
    val quizMarks: Int,
    val created_at: String,
    val updated_at: String,
    val instructions: List<String>
)