package net.rmitsolutions.eskool.network

import net.rmitsolutions.eskool.models.LibraryList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Created by Madhu on 25-Jul-2017.
 */
interface ILibrary {
    @GET("Library/GetLibraryItems")
    fun getLibraryItems(@Header("Authorization") authToken: String): Observable<LibraryList>
}