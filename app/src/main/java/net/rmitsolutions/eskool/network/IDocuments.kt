package net.rmitsolutions.eskool.network

import net.rmitsolutions.eskool.models.CircularsList
import net.rmitsolutions.eskool.models.DocParams
import net.rmitsolutions.eskool.models.DocumentsList
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by Madhu on 12-Jul-2017.
 */
interface IDocuments {
    @POST("Documents/GetDocuments")
    fun getDocuments(@Header("Authorization") authToken: String, @Body docParams: DocParams): Observable<DocumentsList>

    @GET("Documents/GetCirculars")
    fun getCirculars(@Header("Authorization") authToken: String): Observable<CircularsList>
}