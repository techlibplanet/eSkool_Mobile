package net.rmitsolutions.eskool.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Madhu on 13-Jun-2017.
 */

class Student(@SerializedName("userId") val userId: Int,
              @SerializedName("studentId") val studentId: Int,
              @SerializedName("code") val code: String,
              @SerializedName("name") val name: String,
              @SerializedName("dob") val dob: Date,
              @SerializedName("gender") private val gender: Byte,
              @SerializedName("studentClassId") val studentClassId: Int,
              @SerializedName("syllabus") val syllabus: String,
              @SerializedName("grade") val grade: String,
              @SerializedName("section") val section: String,
              @SerializedName("academicYear") val academicYear: String,
              @SerializedName("school") val school: String,
              @SerializedName("imageUrl") val imageUrl: String,
              @SerializedName("isFirstLogin") var isFirstLogin: Int,
              @SerializedName("email") val email: String,
              @SerializedName("mobile") val mobile: String,
              @SerializedName("schoolId") val schoolId: Int,
              @SerializedName("academicYearId") val academicYearId: Int) : BaseModel() {

    val genderName: String
        get() = if (gender == 0.toByte()) "Girl" else "Boy"
}

class StudentProfile(@SerializedName("parents") val parents: List<StudentParent>,
                     @SerializedName("address") val address: StudentAddress?) : BaseModel()

class StudentParent(@SerializedName("name") val name: String,
                    @SerializedName("mobile") val mobile: String,
                    @SerializedName("email") val email: String,
                    @SerializedName("relation") val relation: String)

class StudentAddress(@SerializedName("houseNo") val houseNo: String,
                     @SerializedName("street") val street: String,
                     @SerializedName("city") val city: String,
                     @SerializedName("district") val district: String,
                     @SerializedName("state") val state: String,
                     @SerializedName("pincode") val pincode: String,
                     @SerializedName("phone") val phone: String)
