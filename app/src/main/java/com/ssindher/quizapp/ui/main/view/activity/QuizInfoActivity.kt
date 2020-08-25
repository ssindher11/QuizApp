package com.ssindher.quizapp.ui.main.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bugfender.sdk.Bugfender
import com.ssindher.quizapp.R
import com.ssindher.quizapp.utils.AppConstants.DURATION
import com.ssindher.quizapp.utils.AppConstants.QUIZ_ID
import com.ssindher.quizapp.utils.AppConstants.TITLE
import com.ssindher.quizapp.utils.AppConstants.USER_ID
import kotlinx.android.synthetic.main.activity_quiz_info.*

class QuizInfoActivity : AppCompatActivity() {

    private lateinit var i: Intent
    private var duration = ""
    private var quizID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_info)

        setupUI()
        setupListeners()
        Bugfender.d("Activity", "QuizInfoActivity")
    }

    private fun setupUI() {
        i = intent
        textViewTitleQuizInfo.text = i.extras?.get(TITLE).toString()
        duration = i.extras?.get(DURATION).toString()
        textViewDurationQuizInfo.text = "$duration Minutes"
        quizID = i.extras?.get(QUIZ_ID).toString()
    }

    private fun setupListeners() {
        imageViewCloseQuizInfo.setOnClickListener { super.onBackPressed() }

        checkboxAgreementQuizInfo.setOnCheckedChangeListener { _, b ->
            if (b) {
                buttonStartQuizInfo.isClickable = true
                buttonStartQuizInfo.setOnClickListener {
                    Bugfender.d("Action", "Start Quiz")
                    Bugfender.setDeviceString("user.uid", USER_ID)
                    Bugfender.setDeviceString("quiz.id", quizID)
                    val intent = Intent(this@QuizInfoActivity, QuizActivity::class.java)
                    intent.putExtra(DURATION, duration)
                    intent.putExtra(QUIZ_ID, quizID)
                    startActivity(intent)
                }
            } else {
                buttonStartQuizInfo.isClickable = false
            }
        }
    }
}