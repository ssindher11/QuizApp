package com.ssindher.quizapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ssindher.quizapp.R
import com.ssindher.quizapp.data.automodels.Completed
import com.ssindher.quizapp.utils.Utils
import kotlinx.android.synthetic.main.item_quiz.view.*

class CompletedQuizAdapter(private val completedList: ArrayList<Completed>) :
    RecyclerView.Adapter<CompletedQuizAdapter.CompletedQuizViewHolder>() {

    class CompletedQuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(completed: Completed) {
            itemView.apply {
                textViewQuizTitle.text = completed.quizDetails[0].name
                textViewQuizDuration.text = "${completed.quizDetails[0].duration.toString()} mins"
                val startTime = completed.scheduleStart
                val endTime = completed.scheduleEnd
                textViewQuizTimings.text = Utils.parseDate(startTime, endTime)
                imageViewQuizDetails.setOnClickListener {
                    Toast.makeText(itemView.context, "Details of the Quiz", Toast.LENGTH_SHORT)
                        .show()
                }
                imageViewQuizStart.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedQuizViewHolder =
        CompletedQuizViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_quiz, parent, false)
        )

    override fun getItemCount(): Int = completedList.size

    override fun onBindViewHolder(holder: CompletedQuizViewHolder, position: Int) {
        holder.bind(completedList[position])
    }

    fun addQuizzes(completedList: List<Completed>) {
        this.completedList.apply {
            clear()
            addAll(completedList)
        }
    }
}