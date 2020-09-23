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

    const val tempString =
        "<p width=\"100%\"><br><img class=\"fr-dib fr-fil\" src=\"https://winuall-lms.s3.ap-south-1.amazonaws.com/files/edsejLJyLsV3YSIiOfu5CsBS9OHR6EEK0yt2vvPK.png#674*50\" style=\"width: 408px;\" max-height=\"95%\" max-width=\"95%\"></p>"

    fun getScaledImageIfTooLarge(s: String): String {
        val t1 = s.split("width: ")
        val width = t1[1].split("px")[0].toInt()

        return if (width <= 200) {
            s
        } else {
            t1[0] + "width: 200px" + t1[1].split("px")[1]
        }
    }
}