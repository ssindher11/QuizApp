package com.ssindher.quizapp.ui.main.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.ssindher.quizapp.R
import com.ssindher.quizapp.data.api.ApiHelper
import com.ssindher.quizapp.data.api.RetrofitBuilder
import com.ssindher.quizapp.data.quizmodels.Data
import com.ssindher.quizapp.ui.base.ViewModelFactory
import com.ssindher.quizapp.ui.main.view.fragment.FlagBottomSheetDialogFragment
import com.ssindher.quizapp.ui.main.viewmodel.MainViewModel
import com.ssindher.quizapp.utils.AppConstants.DURATION
import com.ssindher.quizapp.utils.Status
import com.ssindher.quizapp.utils.Utils
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_flag.*
import kotlinx.android.synthetic.main.item_quiz.*

class QuizActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var questionDataList: List<Data>
    private lateinit var countDownTimer: CountDownTimer
    private var timeInMs = 0L
    private var startTime = 0L

    private var isConnected = false
    private var questionIndex = 0
    private var selectedIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        setupNetworkListener()
        setupViewModel()
        setupObservers()
        setupOnClickListeners()

//        startTime = intent.extras?.get(DURATION).toString().toLong() * 60000L
        timeInMs = 180 * 60000L
        setupTimer()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.quizApiService))
        ).get(MainViewModel::class.java)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupUI(index: Int) {
        textViewQuestionNumberQuiz.text = (index + 1).toString()
        textViewTypeQuiz.text = questionDataList[index].questionType
        webViewQuestion.loadDataWithBaseURL(
            "",
            Utils.getQuestionHtml(questionDataList[index].body),
            "text/html",
            "UTF-8",
            null
        )
        webViewO1Quiz.loadDataWithBaseURL(
            "",
            Utils.getHtml(questionDataList[index].options[0].value),
            "text/html",
            "UTF-8",
            null
        )
        webViewO2Quiz.loadDataWithBaseURL(
            "",
            Utils.getHtml(questionDataList[index].options[1].value),
            "text/html",
            "UTF-8",
            null
        )
        webViewO3Quiz.loadDataWithBaseURL(
            "",
            Utils.getHtml(questionDataList[index].options[2].value),
            "text/html",
            "UTF-8",
            null
        )
        webViewO4Quiz.loadDataWithBaseURL(
            "",
            Utils.getHtml(questionDataList[index].options[3].value),
            "text/html",
            "UTF-8",
            null
        )
        webViewO1Quiz.settings.javaScriptEnabled = true
        webViewO2Quiz.settings.javaScriptEnabled = true
        webViewO3Quiz.settings.javaScriptEnabled = true
        webViewO4Quiz.settings.javaScriptEnabled = true
    }

    private fun setupQuestionData(dataList: List<Data>) {
        questionDataList = dataList
        setupUI(questionIndex)
    }

    private fun setupObservers() {
        viewModel.getQuiz().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        imageViewNotFoundQuiz.visibility = View.GONE
                        textViewNotFoundQuiz.visibility = View.GONE
                        resource.data?.let { q2 -> setupQuestionData(q2.data) }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@QuizActivity, it.message, Toast.LENGTH_SHORT).show()
                        imageViewNotFoundQuiz.visibility = View.VISIBLE
                        textViewNotFoundQuiz.visibility = View.VISIBLE
                        textViewTypeQuiz.visibility = View.GONE
                        textViewQuestionNumberQuiz.visibility = View.GONE
                        textViewChooseAnOptionLabelQuiz.visibility = View.GONE
                        webViewQuestion.visibility = View.GONE
                        webViewO1Quiz.visibility = View.GONE
                        webViewO2Quiz.visibility = View.GONE
                        webViewO3Quiz.visibility = View.GONE
                        webViewO4Quiz.visibility = View.GONE
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })
    }

    private fun setupOnClickListeners() {
        buttonNextQuiz.setOnClickListener { nextQuestion() }
        buttonPreviousQuiz.setOnClickListener { previousQuestion() }
        imageViewFlagQuiz.setOnClickListener { setupBottomSheet() }

        view01Quiz.setOnClickListener {
            selectedIndex = 0
            Log.d("SelectedIndexTag", selectedIndex.toString())
            markSelectedOption(webViewO1Quiz, webViewO2Quiz, 1, webViewO3Quiz, 2, webViewO4Quiz, 3)
        }
        view02Quiz.setOnClickListener {
            selectedIndex = 1
            Log.d("SelectedIndexTag", selectedIndex.toString())
            markSelectedOption(webViewO2Quiz, webViewO1Quiz, 0, webViewO3Quiz, 2, webViewO4Quiz, 3)
        }
        view03Quiz.setOnClickListener {
            selectedIndex = 2
            Log.d("SelectedIndexTag", selectedIndex.toString())
            markSelectedOption(webViewO3Quiz, webViewO2Quiz, 1, webViewO1Quiz, 0, webViewO4Quiz, 3)
        }
        view04Quiz.setOnClickListener {
            selectedIndex = 3
            Log.d("SelectedIndexTag", selectedIndex.toString())
            markSelectedOption(webViewO4Quiz, webViewO2Quiz, 1, webViewO3Quiz, 2, webViewO1Quiz, 0)
        }
    }

    private fun setupBottomSheet() {
        val bottomSheetDialogFragment = FlagBottomSheetDialogFragment()
        bottomSheetDialogFragment.show(supportFragmentManager, FlagBottomSheetDialogFragment.TAG)
    }

    private fun setupNetworkListener() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                toggleNoInternet(true)
            }

            override fun onUnavailable() {
                toggleNoInternet(true)
            }

            override fun onAvailable(network: Network) {
                toggleNoInternet(false)
            }
        }
        cm.registerDefaultNetworkCallback(networkCallback)
    }

    private fun toggleNoInternet(b: Boolean) {
        isConnected = b
        runOnUiThread {
            if (b) {
                textViewInternetDisconnectedQuiz.visibility = View.VISIBLE
            } else {
                textViewInternetDisconnectedQuiz.visibility = View.GONE
            }
        }
    }

    private fun nextQuestion() {
        questionIndex += 1
        if (questionIndex == questionDataList.size - 1) {
            buttonNextQuiz.text = getString(R.string.finish)
        }
        if (questionIndex < questionDataList.size) {
            setupUI(questionIndex)
        } else {
            // TODO: Finish the quiz
        }
    }

    private fun previousQuestion() {
        if (questionIndex == questionDataList.size - 1) {
            buttonNextQuiz.text = getString(R.string.next)
        }
        if (questionIndex != 0) {
            questionIndex -= 1
        }
        setupUI(questionIndex)
    }

    private fun markSelectedOption(
        selected: WebView,
        v1: WebView,
        i1: Int,
        v2: WebView,
        i2: Int,
        v3: WebView,
        i3: Int
    ) {
        selected.loadDataWithBaseURL("javascript:window.location.reload( true )", "", "text/html", "UTF-8",null)
        v1.loadDataWithBaseURL("javascript:window.location.reload( true )", "", "text/html", "UTF-8",null)
        v2.loadDataWithBaseURL("javascript:window.location.reload( true )", "", "text/html", "UTF-8",null)
        v3.loadDataWithBaseURL("javascript:window.location.reload( true )", "", "text/html", "UTF-8",null)
        selected.loadDataWithBaseURL(
            "",
            Utils.getSelectedHtml(questionDataList[questionIndex].options[selectedIndex].value),
            "text/html",
            "UTF-8",
            null
        )
        v1.loadDataWithBaseURL(
            "",
            Utils.getHtml(questionDataList[questionIndex].options[i1].value),
            "text/html",
            "UTF-8",
            null
        )
        v2.loadDataWithBaseURL(
            "",
            Utils.getHtml(questionDataList[questionIndex].options[i2].value),
            "text/html",
            "UTF-8",
            null
        )
        v3.loadDataWithBaseURL(
            "",
            Utils.getHtml(questionDataList[questionIndex].options[i3].value),
            "text/html",
            "UTF-8",
            null
        )
    }

    private fun setupTimer() {
        countDownTimer = object : CountDownTimer(timeInMs, 1000) {
            override fun onFinish() {
                Toast.makeText(this@QuizActivity, "Time Over", Toast.LENGTH_SHORT).show()
            }

            override fun onTick(p0: Long) {
                timeInMs = p0
                updateTextUI()
            }
        }
        countDownTimer.start()
    }

    private fun updateTextUI() {
        val minutes = (timeInMs / 1000) / 60
        val seconds = (timeInMs / 1000) % 60
        textViewClockQuiz.text = "$minutes min:$seconds sec"
    }
}