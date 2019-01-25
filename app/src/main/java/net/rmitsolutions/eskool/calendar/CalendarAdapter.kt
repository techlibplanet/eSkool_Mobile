package net.rmitsolutions.eskool.calendar

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.databinding.CalendarDataBinding
import net.rmitsolutions.eskool.models.Event
import net.rmitsolutions.eskool.models.EventViewModel
import net.rmitsolutions.eskool.widgets.CalendarEventView
import java.util.*

/**
 * Created by Madhu on 18-Aug-17.
 */
class CalendarAdapter : RecyclerView.Adapter<CalendarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {

        context = parent.context
        red = context.resources.getColor(R.color.light_red_shade)
        green = context.resources.getColor(R.color.light_green_shade)
        val dataBinding = CalendarDataBinding.inflate(LayoutInflater.from(context), parent, false)
        return CalendarViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {


        val model = items[position]
        val day = Calendar.getInstance()
        day.time = model.eventDate

        holder.dataBinding.viewModel = EventViewModel(
                DateFormat.format("dd", day).toString(),
                DateFormat.format("EE", day).toString())

        val eveContainer = holder.dataBinding.eventDescriptionContainer
        eveContainer.removeAllViews()

        for (i in model.events.indices) {
            if (i % 2 == 0) {
                model.events[i].color = green
            } else {
                model.events[i].color = red
            }
            eveContainer.addView(CalendarEventView(context, model.events[i]))
        }
    }

    private lateinit var context: Context
    private var green = 0
    private var red = 0

    var items = emptyList<Event>()

    override fun getItemCount() = items.size


}