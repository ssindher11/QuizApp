package com.ssindher.quizapp.ui.main.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ssindher.quizapp.R
import kotlinx.android.synthetic.main.fragment_bottom_sheet_flag.*


class FlagBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        val TAG = "FlagBottomSheetDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_flag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        imageViewCloseFlag.setOnClickListener { this.dismiss() }
        buttonSubmitFlag.setOnClickListener {
            Toast.makeText(this.context, "Report Submitted!", Toast.LENGTH_SHORT).show()
            this.dismiss()
        }
    }

}