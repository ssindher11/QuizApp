package com.ssindher.quizapp.ui.main.view.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ssindher.quizapp.R
import com.ssindher.quizapp.utils.AppConstants.DURATION
import com.ssindher.quizapp.utils.AppConstants.QUIZ_ID
import com.ssindher.quizapp.utils.AppConstants.TITLE
import com.surveymonkey.surveymonkeyandroidsdk.SurveyMonkey
import com.surveymonkey.surveymonkeyandroidsdk.utils.SMError
import kotlinx.android.synthetic.main.activity_quiz_info.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class QuizInfoActivity : AppCompatActivity() {

    private lateinit var i: Intent
    private var duration = ""
    private var quizID = ""
    private val TAG = "QuizInfoActivityTAG"
    private lateinit var prefs: SharedPreferences

    private lateinit var smSDK: SurveyMonkey

    var theme = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        intent.getStringExtra("theme")?.let { theme = it }
        when (theme) {
            "Red" -> setTheme(R.style.RedTheme)
            "Green" -> setTheme(R.style.GreenTheme)
            "" -> Toast.makeText(this, "Empty Theme", Toast.LENGTH_SHORT).show()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_info)

        setupUI()
        setupListeners()

        smSDK = SurveyMonkey()
        smSDK.onStart(this, "Quiz App", 1001, "KGTGZF5")
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

        checkboxAgreementQuizInfo.setOnCheckedChangeListener { button, b ->
            if (b) {
                buttonStartQuizInfo.isClickable = true
                buttonStartQuizInfo.setOnClickListener {
                    val intent = Intent(this@QuizInfoActivity, QuizActivity::class.java)
                    intent.putExtra(DURATION, duration)
                    intent.putExtra(QUIZ_ID, quizID)
                    startActivity(intent)
                }
            } else {
                buttonStartQuizInfo.isClickable = false
//                smSDK.startSMFeedbackActivityForResult(this, 1001, "KGTGZF5")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            var isPromoter = false
            try {
                val respondent = intent.getStringExtra("smRespondent")
                Log.d(TAG, "$respondent")
                val surveyResponse = JSONObject(respondent)
                val responsesList: JSONArray = surveyResponse.getJSONArray("responses")
                var response: JSONObject
                var answers: JSONArray
                var currentAnswer: JSONObject
                for (i in 0 until responsesList.length()) {
                    response = responsesList.getJSONObject(i)
                    if (response.getString("question_id") == "813797519") {
                        answers = response.getJSONArray("answers")
                        for (j in 0 until answers.length()) {
                            currentAnswer = answers.getJSONObject(j)
                            if (currentAnswer.getString("row_id") == "9082377273" || currentAnswer.getString(
                                    "row_id"
                                ) == "9082377274"
                            ) {
                                isPromoter = true
                                break
                            }
                        }
                        if (isPromoter) {
                            break
                        }
                    }
                }
            } catch (e: JSONException) {
                Log.getStackTraceString(e)
            }
            if (isPromoter) {
                Toast.makeText(this, "IS PROMOTER", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "IS NOT PROMOTER", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show()
            val e: SMError = intent.getSerializableExtra("smError") as SMError
            Log.d(TAG, "SM-Error: ${e.getDescription()}")
        }
    }

}