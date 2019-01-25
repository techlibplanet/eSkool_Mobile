package net.rmitsolutions.eskool.network


import io.reactivex.Observable
import net.rmitsolutions.eskool.models.VersionsModel
import retrofit2.http.GET


/**
 * Created by Geetha on 08-Mar-18.
 */
interface IAssessments {
    @GET("android/jsonandroid")
    fun getVersionsData() : Observable<VersionsModel>
}