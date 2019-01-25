package net.rmitsolutions.eskool.models

import com.google.gson.annotations.SerializedName
import net.rmitsolutions.eskool.R
import java.util.*

/**
 * Created by Madhu on 12-Jul-2017.
 */

enum class FileType(value: Int) {
    Nothing(0), Worksheet(1), Homework(2), Circular(3), Portion(4)
}

class DocParams(@SerializedName("day") val day: Date,
                @SerializedName("fileType") val fileType: FileType)

class DocumentsList(@SerializedName("docs") val docs: List<Document>) : BaseModel()

class Document(@SerializedName("id") val id: Int,
               @SerializedName("subject") val subject: String,
               @SerializedName("documentName") val documentName: String,
               @SerializedName("description") val description: String,
               @SerializedName("docPath") val docPath: String,
               var imgSrc: Int = R.drawable.ic_download)

class DownloadFile(@SerializedName("id") val id: Int,
                   @SerializedName("fileName") val fileName: String,
                   @SerializedName("fileType") val fileType: FileType)
