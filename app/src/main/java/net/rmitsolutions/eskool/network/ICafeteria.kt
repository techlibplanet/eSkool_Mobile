package net.rmitsolutions.eskool.network

import net.rmitsolutions.eskool.models.CafeteriaMenuList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * Created by Madhu on 27-Jul-2017.
 */
interface ICafeteria {
    @GET("Cafeteria/GetCafeteriaMenu/{day}")
    fun getCafeteriaMenu(@Header("Authorization") authToken: String, @Path("day") day: String): Observable<CafeteriaMenuList>
}