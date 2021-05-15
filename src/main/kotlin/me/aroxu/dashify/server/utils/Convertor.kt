package me.aroxu.dashify.server.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Convertor {
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }

    fun convertLongToDuration(duration: Long): String {
        return String.format(
            "%dd%dh%dm%ds",
            TimeUnit.MILLISECONDS.toDays(duration),
            TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(
                TimeUnit.MILLISECONDS.toDays(
                    duration
                )
            ),
            TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    duration
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    duration
                )
            )
        )
    }
}