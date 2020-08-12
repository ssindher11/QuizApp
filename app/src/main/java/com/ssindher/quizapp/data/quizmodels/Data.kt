package com.ssindher.quizapp.data.quizmodels

data class Data(
    val _id: String,
    val body: String,
    val chapter: Chapter,
    val created_at: String,
    val difficulty: Int,
    val isMultiple: Boolean,
    val isPassage: Boolean,
    val negativeMarks: Int,
    val options: List<Option>,
    val orgId: String,
    val partialMarks: Int,
    val positiveMarks: Int,
    val questionType: String,
    val solution: String,
    val subject: Subject,
    val subtopics: List<Subtopic>
)