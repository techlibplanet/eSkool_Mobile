package net.rmitsolutions.eskool.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Geetha on 2/8/2018.
 */
@Entity(tableName = "calender_events")
class CalenderEventEntity(@PrimaryKey
                          @SerializedName("id") var id: Long,
                          @SerializedName("eventDate") var eventDate: Date,
                          @SerializedName("description") val description: String,
                          @SerializedName("time") val time: String,
                          var color: Int = 0
                    )