package com.ssindher.quizapp.ui.main.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.ssindher.quizapp.R
import com.ssindher.quizapp.data.allquizzesmodels.Ongoing
import com.ssindher.quizapp.ui.main.view.activity.QuizInfoActivity
import com.ssindher.quizapp.utils.AppConstants.DURATION
import com.ssindher.quizapp.utils.AppConstants.QUIZ_ID
import com.ssindher.quizapp.utils.AppConstants.TITLE
import com.ssindher.quizapp.utils.Utils
import kotlinx.android.synthetic.main.item_quiz.view.*

class OngoingQuizAdapter(private val quizzes: ArrayList<Ongoing>) :
    RecyclerView.Adapter<OngoingQuizAdapter.OngoingQuizViewHolder>() {

    companion object {
        var theme = "Red"
        lateinit var ctx: Context
    }

    class OngoingQuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(ongoing: Ongoing) {
            itemView.apply {
                ctx = context
                textViewQuizTitle.text = ongoing.quizDetails[0].name
                textViewQuizDuration.text = "${ongoing.quizDetails[0].duration} mins"
                val startTime = ongoing.scheduleStart
                val endTime = ongoing.scheduleEnd
                textViewQuizTimings.text = Utils.parseDate(startTime, endTime)
                imageViewQuizDetails.setOnClickListener {
                    Toast.makeText(itemView.context, "Details of the Quiz", Toast.LENGTH_SHORT)
                        .show()
                }
                imageViewQuizStart.setOnClickListener {
                    val orientationOptions = arrayOf("Red", "Green")
                    var selectedItem = -1
                    AlertDialog.Builder(ctx)
                        .setTitle("Select Theme")
                        .setSingleChoiceItems(
                            orientationOptions, selectedItem
                        ) { _, p1 ->
                            selectedItem = p1
                        }
                        .setPositiveButton("Start") { _, _ ->
                            if (selectedItem == -1) {
                                Toast.makeText(ctx, "Select a theme!", Toast.LENGTH_SHORT).show()
                            } else {
                                theme = orientationOptions[selectedItem]
                                val intent = Intent(ctx, QuizInfoActivity::class.java)
                                intent.putExtra(TITLE, ongoing.quizDetails[0].name)
                                intent.putExtra(DURATION, ongoing.quizDetails[0].duration)
                                intent.putExtra(QUIZ_ID, ongoing.quizDetails[0]._id)
                                intent.putExtra("theme", theme)
                                context.startActivity(intent)
                            }
                        }
                        .setNegativeButton("Cancel") { p0, _ ->
                            p0.dismiss()
                        }
                        .setCancelable(false)
                        .create()
                        .show()
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