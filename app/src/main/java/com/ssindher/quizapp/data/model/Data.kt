package com.ssindher.quizapp.data.model

data class Data(
    val _id: String,
    val body: String,
    val orgId: String,
    val chapter: Chapter,
    val options: List<Option>,
    val subject: Subject,
    val solution: String,
    val isPassage: Boolean,
    val subtopics: List<String>?,
    val created_at: String,
    val difficulty: Int,
    val isMultiple: Boolean,
    val partialMarks: Float,
    val questionType: String,
    val negativeMarks: Float,
    val positiveMarks: Float
)