package com.ssindher.quizapp.data.allquizzesmodels

data class BatchX(
    val completed: List<Completed>,
    val ongoing: List<Ongoing>,
    val upcoming: List<Completed>
)