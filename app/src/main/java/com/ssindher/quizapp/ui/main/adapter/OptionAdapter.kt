package com.ssindher.quizapp.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssindher.quizapp.R
import com.ssindher.quizapp.data.model.Answer
import com.ssindher.quizapp.data.quizmodels.Option
import com.ssindher.quizapp.ui.main.view.activity.QuizActivity
import com.ssindher.quizapp.utils.Utils
import kotlinx.android.synthetic.main.item_option.view.*

data class OptionAdapter(val options: ArrayList<Option>, var checkedItemId: String?) :
    RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var op: Option

        @SuppressLint("SetJavaScriptEnabled")
        fun bind(option: Option) {
            op = option
            var checked = false

            itemView.apply {
                if (option._id != checkedItemId) {
                    webViewOption.loadDataWithBaseURL(
                        "", Utils.getHtml(option.value), "text/html",
                        "UTF-8",
                        null
                    )
                } else {
                    webViewOption.loadDataWithBaseURL(
                        "", Utils.getSelectedHtml(option.value), "text/html",
                        "UTF-8",
                        null
                    )
                    checked = true
                }
                webViewOption.settings.javaScriptEnabled = true
                viewOption.setOnClickListener {
                    checked = !checked
                    // Reset the webView
                    webViewOption.loadDataWithBaseURL(
                        "javascript:window.location.reload( true )",
                        "",
                        "text/html",
                        "UTF-8",
                        null
                    )
                    if (checked) {
                        webViewOption.loadDataWithBaseURL(
                            "", Utils.getSelectedHtml(option.value), "text/html",
                            "UTF-8",
                            null
                        )
                        QuizActivity.markAnswerInterface.mark(Answer(option._id, option.correct))
                    } else {
                        webViewOption.loadDataWithBaseURL(
                            "", Utils.getHtml(option.value), "text/html",
                            "UTF-8",
                            null
                        )
                        QuizActivity.markAnswerInterface.mark(null)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder =
        OptionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_option, parent, false
            )
        )

    override fun getItemCount(): Int = options.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(options[position])
    }

    fun addOptions(options: List<Option>) {
        this.options.apply {
            clear()
            addAll(options)
        }
    }

    fun markItem(checkedId: String?) {
        this.checkedItemId = checkedId
    }
}