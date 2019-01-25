package net.rmitsolutions.eskool.filedownload

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.app.NotificationCompat
import android.support.v4.content.FileProvider
import android.support.v4.content.LocalBroadcastManager
import android.webkit.MimeTypeMap
import net.rmitsolutions.eskool.ESkoolApplication
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.dependency.components.DaggerServiceComponent
import net.rmitsolutions.eskool.helpers.Constants
import net.rmitsolutions.eskool.helpers.ProcessThrowable
import net.rmitsolutions.eskool.helpers.apiAccessToken
import net.rmitsolutions.eskool.helpers.isExternalStorageWritable
import net.rmitsolutions.eskool.models.DownloadFile
import net.rmitsolutions.eskool.models.DownloadParcelable
import net.rmitsolutions.eskool.models.FileType
import net.rmitsolutions.eskool.network.IFileDownload
import okhttp3.ResponseBody
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import javax.inject.Inject


/**
 * Created by Madhu on 15-Jul-2017.
 */
class FileDownloadService : IntentService("eSkool file download service") {
    @Inject
    lateinit var fileDownloadService: IFileDownload
    private var decimalFormat = DecimalFormat("##0.00")
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private var totalFileSize: Float = 0f
    private var fileLength: Long = 0
    private lateinit var outputFile: File
    private var fileName: String = ""
    private var id = 0
    private var studentClassId = 0

    private companion object {
        private var isDownloadInProgress = false
    }

    init {
        val dipComponent = DaggerServiceComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        dipComponent.injectFileDownloadService(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        if (isDownloadInProgress) return
        isDownloadInProgress = true

        fileName = intent?.getStringExtra("fileName") ?: ""
        id = intent?.getIntExtra("id", 0) ?: 0
        val docType = intent?.getSerializableExtra("fileType") as FileType
        studentClassId = intent.getIntExtra("studentClassId", 0)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "file_download_channel"
        notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.ic_download_white)
                .setContentTitle("Download")
                .setAutoCancel(true)
        showNotification("Downloading $fileName ...")

        val response = fileDownloadService.downloadFile(applicationContext.apiAccessToken, DownloadFile(id, fileName, docType)).execute()
        if (response.isSuccessful) {
            val headers = response.headers()
            fileLength = headers.get("fileLength")?.toLong() ?: 0
            downloadFile(response.body())
        } else {
            showNotification(ProcessThrowable.getMessage(response.errorBody()) ?: "File download failed.")
            notifyCaller(false)
        }
    }

    private fun downloadFile(body: ResponseBody?) {
        if (body == null) {
            showNotification("Download failed.")
            notifyCaller(false)
            return
        }

        if (!baseContext.isExternalStorageWritable) {
            showNotification("No storage available to download.")
            notifyCaller(false)
            return
        }
        try {
            val data = ByteArray(1024 * 4)
            val fileSize = fileLength
            val bis = BufferedInputStream(body.byteStream(), 1024 * 8)

            var filesDir = File(Environment.getExternalStorageDirectory(), Constants.DOWNLOAD_MAIN_DIRECTORY)
            if (!filesDir.exists()) filesDir.mkdir()

            filesDir = File(filesDir, Constants.DOWNLOAD_SUB_DIRECTORY)
            if (!filesDir.exists()) filesDir.mkdir()

            filesDir = File(filesDir, studentClassId.toString())
            if (!filesDir.exists()) filesDir.mkdir()

            outputFile = File(filesDir, fileName)
            val output = FileOutputStream(outputFile)
            var total: Long = 0
            val startTime = System.currentTimeMillis()
            var timeCount = 1
            while (true) {
                val count = bis.read(data)
                if (count < 0) break

                total += count.toLong()
                totalFileSize = (fileSize / Math.pow(1024.0, 2.0)).toFloat()
                val current = (total / Math.pow(1024.0, 2.0))
                val progress = (total * 100 / fileSize).toInt()
                val currentTime = System.currentTimeMillis() - startTime
                val download = DownloadParcelable()
                download.totalFileSize = totalFileSize

                if (currentTime > 1000 * timeCount) {
                    download.currentFileSize = current.toFloat()
                    download.progress = progress
                    sendNotification(download)
                    timeCount++
                }

                output.write(data, 0, count)
            }
            onDownloadComplete()
            output.flush()
            output.close()
            bis.close()
        } catch (ex: Exception) {
            showNotification("Download failed.")
            notifyCaller(false)
        }
    }

    private fun onDownloadComplete() {
        val download = DownloadParcelable()
        download.progress = 100

        notificationManager.cancel(0)
        notificationBuilder.setProgress(0, 0, false)

        val fileExt = MimeTypeMap.getFileExtensionFromUrl(fileName)
        val fileType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExt) ?: "*/*"
        val viewFileIntent = Intent(Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val uri: Uri?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewFileIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            uri = FileProvider.getUriForFile(this, getString(R.string.file_provider_name), outputFile)
        } else {
            uri = Uri.fromFile(outputFile)
        }

        viewFileIntent.setDataAndType(uri, fileType)

        val resultPendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                viewFileIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        )
        notificationBuilder.addAction(NotificationCompat.Action(R.drawable.ic_eye, "View", resultPendingIntent))
        showNotification(fileName + " Downloaded")
        notifyCaller(true)
    }

    private fun sendNotification(download: DownloadParcelable) {
        notificationBuilder.setProgress(100, download.progress, false)
        showNotification("Downloading " + fileName + "     " + decimalFormat.format(download.currentFileSize) + "/" + decimalFormat.format(totalFileSize) + " MB")
    }

    private fun showNotification(message: String) {
        notificationBuilder.setContentText(message)
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        notificationManager.cancel(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        isDownloadInProgress = false
    }

    private fun notifyCaller(status: Boolean) {
        val intent = Intent(Constants.DOCUMENT_DOWNLOAD_STATUS_BROADCAST)
        intent.putExtra(Constants.DOCUMENT_DOWNLOAD_ITEM_ID, id)
        intent.putExtra(Constants.DOCUMENT_DOWNLOAD_STATUS, status)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}