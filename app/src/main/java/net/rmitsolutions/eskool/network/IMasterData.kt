package net.rmitsolutions.eskool.network

import net.rmitsolutions.eskool.models.SchoolAddress
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Created by Madhu on 07-Aug-2017.
 */
interface IMasterData {
    @GET("MasterData/GetSchoolAddress")
    fun getSchoolAddress(@Header("Authorization") authToken: String): Observable<SchoolAddress>
}