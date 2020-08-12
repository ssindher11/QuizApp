package com.ssindher.quizapp.data.automodels

data class SettingsXX(
    val attempts: Int,
    val isSuffle: Boolean,
    val `public`: Boolean,
    val security: Boolean,
    val sendMessageToParents: Boolean,
    val sendMessageToStudent: Boolean,
    val solutionReleaseAt: Any?,
    val solutionReleaseType: Int
)