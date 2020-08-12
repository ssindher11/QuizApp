package com.ssindher.quizapp.data.api

import com.ssindher.quizapp.data.automodels.Q1
import com.ssindher.quizapp.data.model.ParticularQuiz
import com.ssindher.quizapp.data.quizmodels.Q2
import retrofit2.http.GET

interface QuizApiService {

    @GET("b00b070fc2461a68383b")
    suspend fun getAllQuizzes(): Q1

    @GET("c454bbb861f9ab1e9309")
    suspend fun getQuiz(): Q2

}