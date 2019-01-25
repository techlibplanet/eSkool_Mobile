package net.rmitsolutions.eskool.network

import net.rmitsolutions.eskool.models.AccessToken
import io.reactivex.Observable
import retrofit2.http.*


/**
 * Created by Madhu on 12-Jun-2017.
 */
interface IUser {
    @FormUrlEncoded
    @POST("connect/token")
    fun validateUser(
            @Field("grant_type") grantType: String,
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("username") userName: String,
            @Field("password") password: String): Observable<AccessToken>
}

