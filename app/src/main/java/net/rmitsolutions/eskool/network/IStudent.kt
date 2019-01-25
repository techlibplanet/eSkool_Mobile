package net.rmitsolutions.eskool.network

import io.reactivex.Observable
import net.rmitsolutions.eskool.models.Student
import net.rmitsolutions.eskool.models.StudentProfile
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Created by Madhu on 16-Jun-2017.
 */
interface IStudent {
    @GET("Student/GetStudent")
    fun getStudent(@Header("Authorization") authToken: String): Observable<Student>

    @GET("Student/GetStudentProfile")
    fun getStudentProfile(@Header("Authorization") authToken: String): Observable<StudentProfile>
}