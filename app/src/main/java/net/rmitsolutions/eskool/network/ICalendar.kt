package net.rmitsolutions.eskool.network

import net.rmitsolutions.eskool.models.CalendarParams
import net.rmitsolutions.eskool.models.EventsList
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by Madhu on 18-Aug-17.
 */
interface ICalendar {
    @POST("Calendar/GetEvents")
    fun getEvents(@Header("Authorization") authToken: String, @Body data: CalendarParams): Observable<EventsList>
}