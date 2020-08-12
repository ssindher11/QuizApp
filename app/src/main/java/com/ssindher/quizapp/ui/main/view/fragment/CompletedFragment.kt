package com.ssindher.quizapp.ui.main.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ssindher.quizapp.R
import com.ssindher.quizapp.data.api.ApiHelper
import com.ssindher.quizapp.data.api.RetrofitBuilder
import com.ssindher.quizapp.data.automodels.Completed
import com.ssindher.quizapp.ui.base.ViewModelFactory
import com.ssindher.quizapp.ui.main.adapter.CompletedQuizAdapter
import com.ssindher.quizapp.ui.main.viewmodel.MainViewModel
import com.ssindher.quizapp.utils.Status
import kotlinx.android.synthetic.main.fragment_completed.*

class CompletedFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var completedQuizAdapter: CompletedQuizAdapter
    private lateinit var viewContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_completed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewContext = view.context
        setupViewModel()
        setupUI()
        setupObservers()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.quizApiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        completedQuizAdapter = CompletedQuizAdapter(arrayListOf())
        recyclerViewCompleted.adapter = completedQuizAdapter
    }

    private fun setupObservers() {
        viewModel.getAllQuizzes().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerViewCompleted.visibility = View.VISIBLE
                        progressBarCompleted.visibility = View.GONE
                        resource.data?.let { q1 ->
                            if (q1.batch.completed.isEmpty()) {
                                recyclerViewCompleted.visibility = View.GONE
                                progressBarCompleted.visibility = View.GONE
                                imageViewNotFoundCompleted.visibility = View.VISIBLE
                                textViewNotFoundCompleted.visibility = View.VISIBLE
                            }
                            retrieveList(q1.batch.completed) }
                    }
                    Status.ERROR -> {
                        recyclerViewCompleted.visibility = View.GONE
                        progressBarCompleted.visibility = View.GONE
                        Toast.makeText(viewContext, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        progressBarCompleted.visibility = View.VISIBLE
                        recyclerViewCompleted.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(completedList: List<Completed>) {
        completedQuizAdapter.apply {
            addQuizzes(completedList)
            notifyDataSetChanged()
        }
    }
}