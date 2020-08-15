package com.ssindher.quizapp.ui.main.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssindher.quizapp.R
import com.ssindher.quizapp.utils.AppConstants.CORRECT
import com.ssindher.quizapp.utils.AppConstants.DURATION
import com.ssindher.quizapp.utils.AppConstants.TIME
import com.ssindher.quizapp.utils.AppConstants.TOTAL_QUESTIONS
import com.ssindher.quizapp.utils.AppConstants.TOTAL_SCORE
import com.ssindher.quizapp.utils.AppConstants.WRONG
import kotlinx.android.synthetic.main.activity_analysis.*

class AnalysisActivity : AppCompatActivity() {

    private var correctQuestions = 0
    private var wrongQuestions = 0
    private var totalQuestions = 0
    private var totalScore = 0
    private var timeRemaining = 0L
    private var duration = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        getIntentData()
        setupUI()
        setupListeners()
    }

    private fun getIntentData() {
        val e = intent.extras
        if (e != null) {
            correctQuestions = e.getInt(CORRECT)
            wrongQuestions = e.getInt(WRONG)
            totalQuestions = e.getInt(TOTAL_QUESTIONS)
            totalScore = e.getInt(TOTAL_SCORE)
            timeRemaining = e.getLong(TIME)
            duration = e.getLong(DURATION)
        }
    }

    private fun setupUI() {
        // accuracy
        val accuracy = correctQuestions * 100 / totalQuestions
        textViewAccuracyAnalysis.text = "$accuracy"
        progressAccuracyAnalysis.progress = accuracy

        textViewCorrectAnalysis.text = "$correctQuestions"
        textViewWrongAnalysis.text = "$wrongQuestions"
        textViewResponseTimeAnalysis.text = getTime()
        textViewQuestionsAttemptedAnalysis.text = "${(correctQuestions + wrongQuestions)}"
        textViewTotalScoreAnalysis.text = "$totalScore"
    }

    private fun setupListeners() {
        textViewBackAnalysis.setOnClickListener { returnHome() }
    }

    private fun getTime(): String {
        val elapsed = duration - timeRemaining
        val minutes = (elapsed / 1000) / 60
        val seconds = (elapsed / 1000) % 60
        return "$minutes min $seconds sec"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        returnHome()
    }

    private fun returnHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAfterTransition()
    }
}