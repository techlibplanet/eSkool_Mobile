package net.rmitsolutions.eskool.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Geetha on 08-Mar-18.
 */
class VersionsModel  (@SerializedName("android") val verList: ArrayList<AndroidVersions>)

class AndroidVersions(@SerializedName("ver") val version: String,
                      @SerializedName("name") val name: String,
                      @SerializedName("api") val api: String)
