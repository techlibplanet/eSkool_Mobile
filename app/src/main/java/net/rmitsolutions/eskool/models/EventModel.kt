package net.rmitsolutions.eskool.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Madhu on 18-Aug-17.
 */

class EventsList(@SerializedName("list") val list: List<Event>) : BaseModel()

class Event(@SerializedName("eventDate") val eventDate: Date,
            @SerializedName("events") val events: List<EventData>)

class EventData(@SerializedName("description") val description: String,
                @SerializedName("time") val time: String,
                var color: Int = 0)

class EventViewModel(val date: String, val day: String)

class CalendarParams(@SerializedName("from") val from: Date,
                     @SerializedName("to") val to: Date)