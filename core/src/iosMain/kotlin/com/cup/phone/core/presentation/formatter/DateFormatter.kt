package com.cup.phone.core.presentation.formatter

import platform.Foundation.NSDate
import platform.Foundation.NSTimeInterval
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.NSDateFormatter

actual class DateFormatter {
    companion object {
        const val TAG = "DateFormatter"
    }

    actual fun parseDate(timestamp: Long): String {
        val time = timestamp.toDouble()
        val date = NSDate.dateWithTimeIntervalSince1970(time as NSTimeInterval)
        val dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "hh:mm:ss a"
        return dateFormatter.stringFromDate(date)
    }
}