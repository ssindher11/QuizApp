package com.ssindher.quizapp.ui.main.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ssindher.quizapp.R
import com.ssindher.quizapp.`interface`.MarkAnswerInterface
import com.ssindher.quizapp.data.api.ApiHelper
import com.ssindher.quizapp.data.api.RetrofitBuilder
import com.ssindher.quizapp.data.model.Answer
import com.ssindher.quizapp.data.quizmodels.Data
import com.ssindher.quizapp.data.quizmodels.Option
import com.ssindher.quizapp.ui.base.ViewModelFactory
import com.ssindher.quizapp.ui.main.adapter.OptionAdapter
import com.ssindher.quizapp.ui.main.view.fragment.FlagBottomSheetDialogFragment
import com.ssindher.quizapp.ui.main.viewmodel.MainViewModel
import com.ssindher.quizapp.utils.AppConstants.CORRECT
import com.ssindher.quizapp.utils.AppConstants.DURATION
import com.ssindher.quizapp.utils.AppConstants.QUESTIONS
import com.ssindher.quizapp.utils.AppConstants.QUESTION_ID
import com.ssindher.quizapp.utils.AppConstants.QUIZ_ID
import com.ssindher.quizapp.utils.AppConstants.SUBMISSIONS
import com.ssindher.quizapp.utils.AppConstants.TIME
import com.ssindher.quizapp.utils.AppConstants.TOTAL_QUESTIONS
import com.ssindher.quizapp.utils.AppConstants.TOTAL_SCORE
import com.ssindher.quizapp.utils.AppConstants.USER_ID
import com.ssindher.quizapp.utils.AppConstants.WRONG
import com.ssindher.quizapp.utils.Status
import com.ssindher.quizapp.utils.Utils
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var questionDataList: List<Data>
    private lateinit var optionAdapter: OptionAdapter
    private var checkHashMap: HashMap<String, Answer?> = LinkedHashMap<String, Answer?>()
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var questionsReference: DatabaseReference
    private lateinit var totalScoreReference: DatabaseReference

    private var timeInMs = 0L
    private var duration = 0L
    private var isDisconnected = false
    private var questionIndex = 0
    private var checkedId: String? = null
    private var quizID = ""
    private var quizEpochTime = System.currentTimeMillis()

    companion object {
        lateinit var markAnswerInterface: MarkAnswerInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        setupNetworkListener()
        setupViewModel()
        setupObservers()
        setupOnClickListeners()

        optionAdapter = OptionAdapter(arrayListOf(), null)
        recyclerViewQuiz.adapter = optionAdapter

        quizID = intent.extras?.get(QUIZ_ID).toString()
        timeInMs = intent.extras?.get(DURATION).toString().toLong() * 60000L
        duration = timeInMs
        setupTimer()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.quizApiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupQuestionData(dataList: List<Data>) {
        questionDataList = dataList
        questionDataList.forEach { data -> checkHashMap[data._id] = null }
        setupUI(questionIndex)
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

        retrieveList(questionDataList[index].options)
        if (checkHashMap[questionDataList[index]._id] != null) {
            checkedId = checkHashMap[questionDataList[index]._id]?.chosenID
            markOption(checkedId!!)
        }
    }

    private fun setupObservers() {
        viewModel.getQuiz().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        imageViewNotFoundQuiz.visibility = View.GONE
                        textViewNotFoundQuiz.visibility = View.GONE
                        recyclerViewQuiz.visibility = View.VISIBLE
                        resource.data?.let { q2 -> setupQuestionData(q2.data) }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@QuizActivity, it.message, Toast.LENGTH_SHORT).show()
                        imageViewNotFoundQuiz.visibility = View.VISIBLE
                        textViewNotFoundQuiz.visibility = View.VISIBLE
                        constraintLayoutQuiz.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        recyclerViewQuiz.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun setupOnClickListeners() {
        buttonNextQuiz.setOnClickListener { nextQuestion() }
        buttonPreviousQuiz.setOnClickListener { previousQuestion() }
        imageViewFlagQuiz.setOnClickListener { setupBottomSheet() }
        buttonFinishQuiz.setOnClickListener { showFinishDialog() }
        imageViewTimelineQuiz.setOnClickListener {
            Toast.makeText(
                this,
                "Total Marks: ${calculateTotal()}",
                Toast.LENGTH_SHORT
            ).show()
        }

        markAnswerInterface = object : MarkAnswerInterface {
            override fun mark(answer: Answer?) {
                checkHashMap[questionDataList[questionIndex]._id] = answer
                if (answer != null) {
                    checkedId = answer.chosenID
                    markOption(checkedId!!)
                }
            }
        }
    }

    private fun setupBottomSheet() {
        val bottomSheetDialogFragment = FlagBottomSheetDialogFragment()
        val bundle = Bundle()
        bundle.putString(QUIZ_ID, quizID)
        bundle.putString(QUESTION_ID, questionDataList[questionIndex]._id)
        bottomSheetDialogFragment.arguments = bundle
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
        isDisconnected = b
        runOnUiThread {
            if (b) {
                textViewInternetDisconnectedQuiz.visibility = View.VISIBLE
            } else {
                textViewInternetDisconnectedQuiz.visibility = View.GONE
            }
        }
    }

    private fun nextQuestion() {
        submitAnswer(
            questionDataList[questionIndex]._id,
            checkHashMap[questionDataList[questionIndex]._id]
        )

        questionIndex += 1
        if (questionIndex == questionDataList.size - 1) {
            buttonNextQuiz.text = getString(R.string.finish)
        }
        if (questionIndex < questionDataList.size) {
            setupUI(questionIndex)
        } else {
            showFinishDialog()
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

    private fun retrieveList(optionsList: List<Option>) {
        optionAdapter.apply {
            addOptions(optionsList)
            notifyDataSetChanged()
        }
    }

    private fun markOption(id: String) {
        optionAdapter.apply {
            markItem(id)
            notifyDataSetChanged()
        }
    }

    private fun calculateTotal(): Int {
        var total = 0
        for ((k, v) in checkHashMap) {
            if (v != null) {
                total += if (v.isCorrect) {
                    questionDataList[questionIndex].positiveMarks
                } else {
                    questionDataList[questionIndex].negativeMarks
                }
            }
        }
        return total
    }

    private fun getCorrectAndWrongAnswers(): Array<Int> {
        val res = arrayOf(0, 0, 0)      // correct, wrong, total
        for ((k, v) in checkHashMap) {
            if (v != null) {
                res[2] += 1
                if (v.isCorrect) {
                    res[0] += 1
                } else {
                    res[1] += 1
                }
            }
        }
        return res
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

    private fun finishQuiz() {
        submitAnswer(
            questionDataList[questionIndex]._id,
            checkHashMap[questionDataList[questionIndex]._id]
        )
        val result = getCorrectAndWrongAnswers()
        val i = Intent(this@QuizActivity, AnalysisActivity::class.java)
        i.putExtra(TOTAL_SCORE, calculateTotal())
        i.putExtra(DURATION, duration)
        i.putExtra(TIME, timeInMs)
        i.putExtra(CORRECT, result[0])
        i.putExtra(WRONG, result[1])
        i.putExtra(TOTAL_QUESTIONS, result[2])
        startActivity(i)
        finishAfterTransition()
    }

    private fun submitAnswer(quesId: String, answer: Answer?) {
        totalScoreReference =
            Firebase.database.reference.child(SUBMISSIONS).child(USER_ID)
                .child(quizEpochTime.toString()).child(quizID)
                .child(TOTAL_SCORE)
        totalScoreReference.setValue(calculateTotal())

        questionsReference =
            Firebase.database.reference.child(SUBMISSIONS).child(USER_ID)
                .child(quizEpochTime.toString()).child(quizID)
                .child(QUESTIONS).child(quesId)
        questionsReference.setValue(answer)
    }

    private fun showFinishDialog() {
        if (isDisconnected) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Please connect to internet to finish the quiz")
            builder.setPositiveButton("OK") { _, _ -> }
            builder.show()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to finish the quiz?")
            builder.setNegativeButton("NO") { _, _ -> }
            builder.setPositiveButton("YES") { _, _ -> finishQuiz() }
            builder.show()
        }
    }

    override fun onBackPressed() {
        showFinishDialog()
    }
}