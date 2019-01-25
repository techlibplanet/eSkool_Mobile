package net.rmitsolutions.eskool.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Madhu on 02-Aug-2017.
 */
class FeeList(@SerializedName("fees") val fees: List<Fee>) : BaseModel()

class Fee(@SerializedName("feeName") val feeName: String,
          @SerializedName("due") val due: Double,
          @SerializedName("discount") val discount: Double,
          @SerializedName("paid") val paid: Double)