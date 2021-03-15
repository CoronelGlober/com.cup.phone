package com.cup.phone.core.presentation.formatter

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

actual class DateFormatter {
    companion object {
        const val TAG = "DateFormatter"
    }

    actual fun parseDate(timestamp: Long): String {
        try {
            val sdf = SimpleDateFormat("hh:mm:ss a")
            val netDate = Date(timestamp)
            return sdf.format(netDate)
        } catch (e: Exception) {
            Log.e(TAG, "error parsing date", e)
            return ""
        }
    }
}