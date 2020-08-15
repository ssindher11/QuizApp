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
import com.ssindher.quizapp.data.allquizzesmodels.Completed
import com.ssindher.quizapp.data.api.ApiHelper
import com.ssindher.quizapp.data.api.RetrofitBuilder
import com.ssindher.quizapp.ui.base.ViewModelFactory
import com.ssindher.quizapp.ui.main.adapter.CompletedQuizAdapter
import com.ssindher.quizapp.ui.main.viewmodel.MainViewModel
import com.ssindher.quizapp.utils.Status
import kotlinx.android.synthetic.main.fragment_upcoming.*

class UpcomingFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var upcomingQuizAdapter: CompletedQuizAdapter
    private lateinit var viewContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upcoming, container, false)
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
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.quizApiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        upcomingQuizAdapter = CompletedQuizAdapter(arrayListOf())
        recyclerViewUpcoming.adapter = upcomingQuizAdapter
    }

    private fun setupObservers() {
        viewModel.getAllQuizzes().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerViewUpcoming.visibility = View.VISIBLE
                        progressBarUpcoming.visibility = View.GONE
                        resource.data?.let { q1 ->
                            if (q1.batch.upcoming.isEmpty()) {
                                recyclerViewUpcoming.visibility = View.GONE
                                progressBarUpcoming.visibility = View.GONE
                                imageViewNotFoundUpcoming.visibility = View.VISIBLE
                                textViewNotFoundUpcoming.visibility = View.VISIBLE
                            }
                            retrieveList(q1.batch.upcoming)
                        }
                    }
                    Status.ERROR -> {
                        recyclerViewUpcoming.visibility = View.GONE
                        progressBarUpcoming.visibility = View.GONE
                        Toast.makeText(viewContext, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        progressBarUpcoming.visibility = View.VISIBLE
                        recyclerViewUpcoming.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(upcomingList: List<Completed>) {
        upcomingQuizAdapter.apply {
            addQuizzes(upcomingList)
            notifyDataSetChanged()
        }
    }
}