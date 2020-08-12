package com.ssindher.quizapp.ui.main.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ssindher.quizapp.R
import com.ssindher.quizapp.data.automodels.Ongoing
import com.ssindher.quizapp.ui.main.view.activity.QuizInfoActivity
import com.ssindher.quizapp.utils.AppConstants.DURATION
import com.ssindher.quizapp.utils.AppConstants.QUIZ_ID
import com.ssindher.quizapp.utils.AppConstants.TITLE
import com.ssindher.quizapp.utils.Utils
import kotlinx.android.synthetic.main.item_quiz.view.*

class OngoingQuizAdapter(private val quizzes: ArrayList<Ongoing>) :
    RecyclerView.Adapter<OngoingQuizAdapter.OngoingQuizViewHolder>() {

    class OngoingQuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(ongoing: Ongoing) {
            itemView.apply {
                textViewQuizTitle.text = ongoing.quizDetails[0].name
                textViewQuizDuration.text = "${ongoing.quizDetails[0].duration} mins"
                val startTime = ongoing.scheduleStart
                val endTime = ongoing.scheduleEnd
                textViewQuizTimings.text = Utils.parseDate(startTime, endTime)
                imageViewQuizDetails.setOnClickListener {
                    Toast.makeText(itemView.context, "Details of the Quiz", Toast.LENGTH_SHORT)
                        .show()
                }
                // TODO: Start the quiz
                imageViewQuizStart.setOnClickListener {
                    val intent = Intent(context, QuizInfoActivity::class.java)
                    intent.putExtra(TITLE, ongoing.quizDetails[0].name)
                    intent.putExtra(DURATION, ongoing.quizDetails[0].duration)
                    intent.putExtra(QUIZ_ID, ongoing.quizDetails[0]._id)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OngoingQuizViewHolder =
        OngoingQuizViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_quiz, parent, false)
        )

    override fun getItemCount(): Int = quizzes.size

    override fun onBindViewHolder(holder: OngoingQuizViewHolder, position: Int) {
        holder.bind(quizzes[position])
    }

    fun addQuizzes(quizzes: List<Ongoing>) {
        this.quizzes.apply {
            clear()
            addAll(quizzes)
        }
    }

}