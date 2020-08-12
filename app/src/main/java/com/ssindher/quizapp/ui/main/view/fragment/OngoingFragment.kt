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
import com.ssindher.quizapp.data.automodels.Ongoing
import com.ssindher.quizapp.ui.base.ViewModelFactory
import com.ssindher.quizapp.ui.main.adapter.OngoingQuizAdapter
import com.ssindher.quizapp.ui.main.viewmodel.MainViewModel
import com.ssindher.quizapp.utils.Status
import kotlinx.android.synthetic.main.fragment_ongoing.*

class OngoingFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var ongoingQuizAdapter: OngoingQuizAdapter
    private lateinit var viewContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ongoing, container, false)
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
        ongoingQuizAdapter = OngoingQuizAdapter(arrayListOf())
        recyclerViewOngoing.adapter = ongoingQuizAdapter
    }

    private fun setupObservers() {
        viewModel.getAllQuizzes().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerViewOngoing.visibility = View.VISIBLE
                        progressBarOngoing.visibility = View.GONE
                        resource.data?.let { q1 ->
                            if (q1.batch.ongoing.isEmpty()) {
                                recyclerViewOngoing.visibility = View.GONE
                                progressBarOngoing.visibility = View.GONE
                                imageViewNotFoundOngoing.visibility = View.VISIBLE
                                textViewNotFoundOngoing.visibility = View.VISIBLE
                            }
                            retrieveList(q1.batch.ongoing)
                        }
                    }
                    Status.ERROR -> {
                        recyclerViewOngoing.visibility = View.GONE
                        progressBarOngoing.visibility = View.GONE
                        Toast.makeText(viewContext, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        progressBarOngoing.visibility = View.VISIBLE
                        recyclerViewOngoing.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(ongoingList: List<Ongoing>) {
        ongoingQuizAdapter.apply {
            addQuizzes(ongoingList)
            notifyDataSetChanged()
        }
    }
}