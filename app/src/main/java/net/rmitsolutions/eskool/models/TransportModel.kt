package net.rmitsolutions.eskool.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Madhu on 31-Jul-2017.
 */

class Transport(@SerializedName("onwardRoute") val onwardRoute: RoutePoint,
                @SerializedName("returnRoute") val returnRoute: RoutePoint,
                @SerializedName("due") val due: Double,
                @SerializedName("discount") val discount: Double) : BaseModel()

class RoutePoint(@SerializedName("routeName") val routeName: String,
                 @SerializedName("pointName") val pointName: String,
                 @SerializedName("vehicle") val vehicle: String,
                 @SerializedName("registrationNo") val registrationNo: String,
                 @SerializedName("vehicleType") val vehicleType: String) {

    val vehicleName: String
        get() = "$vehicle ($vehicleType) - $registrationNo"
}