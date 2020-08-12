package com.ssindher.quizapp.ui.main.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssindher.quizapp.R
import com.ssindher.quizapp.utils.AppConstants.DURATION
import com.ssindher.quizapp.utils.AppConstants.TITLE
import kotlinx.android.synthetic.main.activity_quiz_info.*

class QuizInfoActivity : AppCompatActivity() {

    private lateinit var i: Intent
    private var duration = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_info)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        i = intent
        textViewTitleQuizInfo.text = i.extras?.get(TITLE).toString()
        duration = i.extras?.get(DURATION).toString()
        textViewDurationQuizInfo.text = "$duration Minutes"
    }

    private fun setupListeners() {
        imageViewCloseQuizInfo.setOnClickListener { super.onBackPressed() }

        checkboxAgreementQuizInfo.setOnCheckedChangeListener { button, b ->
            if (b) {
                buttonStartQuizInfo.isClickable = true
                buttonStartQuizInfo.setOnClickListener {
                    val intent = Intent(this@QuizInfoActivity, QuizActivity::class.java)
                    intent.putExtra(DURATION, duration)
                    startActivity(intent) // TODO: pass extras
                }
            } else {
                buttonStartQuizInfo.isClickable = false
            }
        }
    }

}