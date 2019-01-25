package net.rmitsolutions.eskool.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Madhu on 21-Jul-2017.
 */

class CircularsList(@SerializedName("circulars") val circulars: List<Circular>) : BaseModel()

class Circular(@SerializedName("id") val id: Int,
               @SerializedName("uploadDate") val uploadDate: Date,
               @SerializedName("circularName") val circularName: String,
               @SerializedName("circularPath") val circularPath: String,
               @SerializedName("description") val description: String)