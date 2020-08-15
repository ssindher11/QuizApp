package com.ssindher.quizapp.ui.main.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ssindher.quizapp.R
import com.ssindher.quizapp.data.model.FlagReport
import com.ssindher.quizapp.utils.AppConstants.FLAGS
import com.ssindher.quizapp.utils.AppConstants.QUESTION_ID
import com.ssindher.quizapp.utils.AppConstants.QUIZ_ID
import kotlinx.android.synthetic.main.fragment_bottom_sheet_flag.*


class FlagBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        val TAG = "FlagBottomSheetDialog"
    }

    private lateinit var database: DatabaseReference
    private var quizID = ""
    private var questionID = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            quizID = arguments!!.get(QUIZ_ID).toString()
            questionID = arguments!!.get(QUESTION_ID).toString()
        }
        return inflater.inflate(R.layout.fragment_bottom_sheet_flag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        imageViewCloseFlag.setOnClickListener { this.dismiss() }
        buttonSubmitFlag.setOnClickListener {
            submitReport()
        }
    }

    private fun submitReport() {
        val flag = FlagReport()
        flag.quizID = quizID
        flag.quesID = questionID
        flag.subjectIncorrect = switchSubIncorrect.isChecked
        flag.questionIncorrect = switchQuesIncorrect.isChecked
        flag.optionsIncorrect = switchOptIncorrect.isChecked
        flag.reason = textInputEditTextReasonFlag.text.toString()

        database = Firebase.database.reference.child(FLAGS).child(quizID)
        database.push().setValue(flag).addOnSuccessListener {
            Toast.makeText(this.context, "Report Submitted!", Toast.LENGTH_SHORT).show()
            this.dismiss()
        }.addOnFailureListener {
            Toast.makeText(this.context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
        }
    }
}