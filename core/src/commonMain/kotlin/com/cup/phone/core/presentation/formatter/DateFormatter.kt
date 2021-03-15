package com.cup.phone.core.presentation.formatter

expect class DateFormatter() {
    fun  parseDate( timestamp:Long):String
}