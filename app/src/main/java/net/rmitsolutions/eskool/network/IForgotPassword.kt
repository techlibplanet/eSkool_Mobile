package net.rmitsolutions.eskool.network

import io.reactivex.Observable
import net.rmitsolutions.eskool.ResetPasswordModel
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by Geetha on 31-Mar-18.
 */
interface IForgotPassword {
    @POST("/api/User/ResetPassword")
    fun forgotPassword(@Header("Authorization") authToken: String, @Body data: ResetPasswordModel): Observable<Map<String, String>>

}