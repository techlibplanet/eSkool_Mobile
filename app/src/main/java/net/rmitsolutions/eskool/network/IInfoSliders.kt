package net.rmitsolutions.eskool.network

import io.reactivex.Observable
import net.rmitsolutions.eskool.models.SlidersResponse
import retrofit2.http.GET

/**
 * Created by Geetha on 01-Mar-18.
 */
interface IInfoSliders {
    @GET("android/dummy/MultiViewPager.json")
    fun getArtistData() : Observable<SlidersResponse>

}