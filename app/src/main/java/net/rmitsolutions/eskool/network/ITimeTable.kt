package net.rmitsolutions.eskool.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Created by Madhu on 17-Aug-17.
 */
interface ITimeTable {
    @GET("TimeTable/GetTimeTable")
    fun getTimeTable(@Header("Authorization") authToken: String): Observable<ResponseBody>
}