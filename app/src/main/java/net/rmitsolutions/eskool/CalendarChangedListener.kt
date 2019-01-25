package net.rmitsolutions.eskool

import java.util.*

/**
 * Created by Madhu on 10-Jul-2017.
 */
interface CalendarChangedListener {
    fun onDateSelected(selectedDate: Date)
    fun onMonthSelected(firstDayOfNewMonth: Date)
}