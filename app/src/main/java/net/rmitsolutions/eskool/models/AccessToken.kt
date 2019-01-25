package net.rmitsolutions.eskool.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Madhu on 12-Jun-2017.
 */
class AccessToken {
    @SerializedName("access_token")
    var accessToken: String? = null
    @SerializedName("token_type")
    var tokenType: String? = null
    @SerializedName("expires_in")
    var expiresIn: Int = 0
    @SerializedName("refresh_token")
    var refreshToken: String? = null
}