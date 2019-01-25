package net.rmitsolutions.eskool.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Madhu on 21-Aug-17.
 */

class AttendanceParams(@SerializedName("month") val month: Int, @SerializedName("year") val year: Int)

class AttendanceList(@SerializedName("list") val list: List<Attendance>) : BaseModel()

class Attendance(@SerializedName("day") val day: Date,
                 @SerializedName("present") val present: Int,
                 @SerializedName("absent") val absent: Int)

class AttendanceViewModel(var datePresent: String,
                          var dayPresent: String,
                          var dateAbsent: String,
                          var dayAbsent: String)