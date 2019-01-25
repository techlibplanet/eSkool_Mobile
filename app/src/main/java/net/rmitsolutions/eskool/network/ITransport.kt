package net.rmitsolutions.eskool.network

import net.rmitsolutions.eskool.models.Transport
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Created by Madhu on 31-Jul-2017.
 */
interface ITransport {
    @GET("Transport/GetTransportation")
    fun getTransportation(@Header("Authorization") authToken: String): Observable<Transport>
}