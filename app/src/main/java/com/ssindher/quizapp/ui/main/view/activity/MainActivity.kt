package com.ssindher.quizapp.ui.main.view.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.ssindher.quizapp.R
import com.ssindher.quizapp.ui.main.adapter.ViewPagerFragmentAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val tabTitles = listOf("Ongoing", "Upcoming", "Completed")
    private var theme = "Red"

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    private fun init() {
        viewPager.adapter = ViewPagerFragmentAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        viewPager.offscreenPageLimit = 2

        ibTheme.setOnClickListener {
            showSingleChoiceDialog()
        }
    }

    private fun showSingleChoiceDialog() {
        val orientationOptions = arrayOf("Red", "Green")
        var selectedItem = -1
        AlertDialog.Builder(this)
            .setTitle("Select Orientation")
            .setSingleChoiceItems(
                orientationOptions, selectedItem
            ) { _, p1 ->
                selectedItem = p1
            }
            .setPositiveButton("Start") { _, _ ->
                if (selectedItem == -1) {
                    Toast.makeText(this, "Select a theme!", Toast.LENGTH_SHORT).show()
                } else {
                    theme = orientationOptions[selectedItem]
                    with(prefs.edit()) {
                        putString("theme", theme)
                        apply()
                    }
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