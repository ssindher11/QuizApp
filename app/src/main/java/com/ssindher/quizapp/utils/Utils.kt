package com.ssindher.quizapp.utils

import java.time.LocalDate
import java.time.LocalDateTime

object Utils {

    fun parseDate(s1: String, s2: String): String =
        "${getDateFromString(s1)} - ${getDateFromString(s2)}"

    private fun getDateFromString(s: String): String {
        val res = s.split("T")
        val date = res[0]
        val time = s.split(".")[0]
        val ld = LocalDate.parse(date)
        var lt = LocalDateTime.parse(time)
        lt = lt.plusMinutes(330)
        return "${lt.hour}:${lt.minute} hrs,${ld.dayOfMonth}/${ld.monthValue}/${ld.year}"
    }

    fun getQuestionHtml(s: String) : String {
        var html = "<!DOCTYPE html>"
        html += "<head><title></title></head>"
        html += "<body>$s</body>"
        html += "</html>"
        return html
    }

    fun getHtml(s: String): String {
        var html = "<!DOCTYPE html>"
        html += "<head><title></title></head>"
        html += "<body style=\"color:#0188FF\">$s</body>"
        html += "</html>"
        return html
    }

    fun getSelectedHtml(s: String): String {
        var html = "<!DOCTYPE html>"
        html += "<head><title></title></head>"
        html += "<body style=\"background-color:#44F09F; color:White\">$s</body>"
        html += "</html>"
        return html
    }
}