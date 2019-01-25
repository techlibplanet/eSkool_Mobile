package net.rmitsolutions.eskool.network

import io.reactivex.Observable
import net.rmitsolutions.eskool.models.ChangePasswordModel
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by Geetha on 29-Mar-18.
 */
interface IChangePassword {

    @POST("/api/User/ChangePassword")
    fun changePassword(@Header("Authorization") authToken: String, @Body data: ChangePasswordModel): Observable<Map<String,String>>

}