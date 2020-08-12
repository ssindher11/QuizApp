package com.ssindher.quizapp.data.model

data class Settings(
    val public: Boolean,
    val attempts: Int,
    val isSuffle: Boolean,
    val security: Boolean,
    val solutionReleaseAt: String?,
    val solutionReleaseType: Int,
    val sendMessageToParents: Boolean,
    val sendMessageToStudent: Boolean
)