package com.ssindher.quizapp.data.repository

import com.ssindher.quizapp.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getAllQuizzes() = apiHelper.getAllQuizzes()

    suspend fun getQuiz() = apiHelper.getQuiz()

}