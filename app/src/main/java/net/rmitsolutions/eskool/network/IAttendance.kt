package net.rmitsolutions.eskool.network

import net.rmitsolutions.eskool.models.AttendanceList
import net.rmitsolutions.eskool.models.AttendanceParams
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by Madhu on 21-Aug-17.
 */
interface IAttendance {
    @POST("Attendance/GetAttendance")
    fun getAttendance(@Header("Authorization") authToken: String, @Body data: AttendanceParams): Observable<AttendanceList>
}