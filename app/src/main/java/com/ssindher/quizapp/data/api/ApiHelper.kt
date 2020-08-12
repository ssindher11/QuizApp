package com.ssindher.quizapp.data.api

class ApiHelper(private val quizApiService: QuizApiService) {

    suspend fun getAllQuizzes() = quizApiService.getAllQuizzes()

    suspend fun getQuiz() = quizApiService.getQuiz()

}