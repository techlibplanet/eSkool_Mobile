package net.rmitsolutions.eskool.network

import net.rmitsolutions.eskool.models.DownloadFile
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Streaming

/**
 * Created by Madhu on 15-Jul-2017.
 */
interface IFileDownload {
    @POST("Documents/DownloadFile")
    @Streaming
    fun downloadFile(@Header("Authorization") authToken: String, @Body doc: DownloadFile): Call<ResponseBody>
}