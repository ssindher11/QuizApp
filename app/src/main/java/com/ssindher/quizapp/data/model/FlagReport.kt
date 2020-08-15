package com.ssindher.quizapp.data.model

data class FlagReport(
    var quizID: String? = "",
    var quesID: String? = "",
    var subjectIncorrect: Boolean = false,
    var questionIncorrect: Boolean = false,
    var optionsIncorrect: Boolean = false,
    var reason: String? = ""
)