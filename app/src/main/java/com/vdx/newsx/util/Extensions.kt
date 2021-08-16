package com.vdx.newsx.util

import android.content.Context
import android.util.Log
import com.vdx.newsx.R
import java.net.URLDecoder
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun String.convertTime(context: Context): String {
    val date: String = this
    val convertedTime: String
    val suffix = context.resources.getString(R.string.ago)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    var pasTime: Date? = null
    try {
        pasTime = dateFormat.parse(date)
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("TAG", "convertLastTransactionTime: $e")
    }
    val nowTime = Date()
    var dateDiff: Long = 0
    if (pasTime != null) {
        dateDiff = nowTime.time - pasTime.time
    }
    val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
    val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
    val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
    val day = TimeUnit.MILLISECONDS.toDays(dateDiff)
    convertedTime = if (second < 60) {
        if (second < 0) {
            0.toString() + " " + context.resources.getString(R.string.seconds) + " " + suffix
        } else {
            second.toString() + " " + context.resources.getString(R.string.seconds) + " " + suffix
        }
    } else if (minute <= 60) {
        minute.toString() + " " + context.resources.getString(R.string.minutes) + " " + suffix
    } else if (hour < 24) {
        hour.toString() + " " + context.resources.getString(R.string.hours) + " " + suffix
    } else if (hour < 48) {
        context.resources.getString(R.string.yesterday)
    } else if (day >= 7) {
        (day / 7).toString() + " " + context.resources.getString(R.string.week) + " " + suffix
    } else {
        day.toString() + " " + context.resources.getString(R.string.days) + " " + suffix
    }
    return convertedTime
}

fun String.encode(): String {
    return URLEncoder.encode(this, "utf-8")
}

fun String.decode(): String {
    return URLDecoder.decode(this, "utf-8")
}