package net.rmitsolutions.eskool.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Geetha on 22-Feb-18.
 *
 */



class SlidersResponse(@SerializedName("code") val code: Integer,
                     @SerializedName("message") val info: String,
                     @SerializedName("last") val last: Boolean,
                     @SerializedName("arrList") val arrList: ArrayList<ArrList>)


class ArrList(@SerializedName("no") val no: Integer,
        @SerializedName("title") val title: String,
           @SerializedName("thumb") val image: String,
           @SerializedName("desc") val description: String)
