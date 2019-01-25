package net.rmitsolutions.eskool.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import net.rmitsolutions.eskool.helpers.Constants

/**
 * Created by Madhu on 07-Aug-2017.
 */

@Entity(tableName = "school_address")
class SchoolAddress(
        @PrimaryKey
        @SerializedName("id") var id: Int,
        @SerializedName("address1") val address1: String,
        @SerializedName("address2") val address2: String,
        @SerializedName("address3") val address3: String,
        @SerializedName("address4") val address4: String,
        @SerializedName("pincode") val pincode: String,
        @SerializedName("phone1") val phone1: String,
        @SerializedName("phone2") val phone2: String,
        @SerializedName("mobile1") val mobile1: String,
        @SerializedName("mobile2") val mobile2: String,
        @SerializedName("emailId") val emailId: String,
        @SerializedName("webSite") val webSite: String,
        @SerializedName("longitude") val longitude: Float,
        @SerializedName("latitude") val latitude: Float
) : @Ignore BaseModel() {

    var schoolName: String = ""
        get() = Constants.studentModel!!.school

}
