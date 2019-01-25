package net.rmitsolutions.eskool.models

import com.google.gson.annotations.SerializedName
import java.text.DateFormat
import java.util.*

/**
 * Created by Madhu on 24-Jul-2017.
 */

class LibraryList(@SerializedName("items") val items: List<Library>) : BaseModel()

class Library(@SerializedName("itemName") val itemName: String,
              @SerializedName("itemPublisher") val itemPublisher: String,
              @SerializedName("issueDate") val issueDate: Date,
              @SerializedName("dueDate") val dueDate: Date,
              var imgSrc: Int = 0) {
    val displayIssueDate: String
        get() = DateFormat.getDateInstance().format(issueDate)

    val displayDueDate: String
        get() = DateFormat.getDateInstance().format(dueDate)
}