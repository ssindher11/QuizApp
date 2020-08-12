package com.ssindher.quizapp.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ssindher.quizapp.ui.main.view.fragment.CompletedFragment
import com.ssindher.quizapp.ui.main.view.fragment.OngoingFragment
import com.ssindher.quizapp.ui.main.view.fragment.UpcomingFragment

class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> OngoingFragment()
        1 -> UpcomingFragment()
        2 -> CompletedFragment()
        else -> OngoingFragment()
    }

}