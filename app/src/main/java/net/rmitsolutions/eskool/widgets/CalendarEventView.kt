package net.rmitsolutions.eskool.widgets

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.models.EventData


/**
 * Created by Madhu on 18-Aug-17.
 */
class CalendarEventView(context: Context) : LinearLayout(context) {
    private var eventDescText: TextView
    private var eventTimeText: TextView
    private lateinit var event: EventData

    constructor(context: Context, event: EventData) : this(context) {
        this.event = event
        eventDescText.text = event.description
        eventTimeText.text = event.time
        setBackgroundColor(event.color)
    }

    init {
        orientation = LinearLayout.VERTICAL
        setPadding(0, 16, 0, 0)

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.widget_calendar_event_view, this, true)

        eventDescText = getChildAt(0) as TextView
        eventTimeText = getChildAt(1) as TextView
    }

    fun setEventDesc(desc: String) {
        eventDescText.text = desc
    }

    fun setEventTime(time: String) {
        eventDescText.text = time
    }
}