package com.ssindher.quizapp.data.allquizzesmodels

data class SettingsX(
    val attempts: Int,
    val isSuffle: Boolean,
    val `public`: Boolean,
    val security: Boolean,
    val sendMessageToParents: Boolean,
    val sendMessageToStudent: Boolean,
    val solutionReleaseAt: Any?,
    val solutionReleaseType: Int
)