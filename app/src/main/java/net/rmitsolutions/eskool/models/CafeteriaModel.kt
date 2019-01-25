package net.rmitsolutions.eskool.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Madhu on 25-Jul-2017.
 */

class CafeteriaMenuList(@SerializedName("menuList") val menuList: List<CafeteriaMenu>) : BaseModel()

class CafeteriaMenu(@SerializedName("category") val category: String,
                    @SerializedName("menu") val menu: List<String>)