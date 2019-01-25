package net.rmitsolutions.eskool.network

import net.rmitsolutions.eskool.models.FeeList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Created by Madhu on 02-Aug-2017.
 */
interface IFee {
    @GET("Fee/GetFees")
    fun getFees(@Header("Authorization") authToken: String): Observable<FeeList>
}