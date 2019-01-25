package net.rmitsolutions.eskool.helpers

import android.content.Context
import android.content.pm.PackageManager
import net.rmitsolutions.eskool.models.AccessToken
import net.rmitsolutions.eskool.models.SlidersResponse
import net.rmitsolutions.eskool.models.Student
import net.rmitsolutions.eskool.models.StudentProfile
import java.text.DecimalFormat


/**
 * Created by Madhu on 12-Jun-2017.
 */

object Constants {
    //genymotion
    //const val ID_SVR_AUTHORITY = "http://192.168.11.2:5000/"
    //const val API_BASE_ADDRESS = "http://192.168.11.2:5001/api/"

    //server
    const val ID_SVR_AUTHORITY = "http://183.82.2.126:89/"
    const val API_BASE_ADDRESS = "http://183.82.2.126:88/api/"

    //Reqelford server
   // const val ID_SVR_AUTHORITY = "http://103.15.62.250:82/"
//    const val API_BASE_ADDRESS = "http://103.15.62.250:83/api/"
  //  const val API_DEMO_ADDRESS = "http://leejangyoun.com/"
 //   const val API_DEMO_ADDRESS1 = "http://api.learn2crack.com"


    const val API_NAME = "eSApi"
    const val GRANT_TYPE = "password"
    const val CLIENT_ID = "mobile"
    const val CLIENT_SECRET = "eSMobile@rmit"

    const val CONNECTION_TIMEOUT: Long = 60
    const val READ_TIMEOUT: Long = 60

    const val DISPLAY_DATE_FORMAT = "dd-MMM-yyyy"

    var accessToken: AccessToken? = null
    var studentModel: Student? = null
    var infoModel: SlidersResponse? = null
    var studentProfile: StudentProfile? = null

    const val STUDENT_PROFILE_IMAGE_URL = API_BASE_ADDRESS + "Student/GetStudentImage"

    val dashboardModuleDesc = hashMapOf(
            /*0 to "Personal and grade information",
            1 to "Child's daily attendance",
            2 to "School fee details",
            3 to "View and download worksheets",
            4 to "View and download home work",
            5 to "Circulars sent by school",
            6 to "Books to be returned to library",
            7 to "View today's menu",
            8 to "Route and vehicle information",
            9 to "Child's report card",
            10 to "Class time table",
            11 to "School events calendar"*/

            0 to "Personal and grade information",
            1 to "Child's daily attendance",
            2 to "View and download worksheets",
            3 to "View and download home work",
            4 to "Circulars sent by school",
            5 to "Books to be returned to library",
            6 to "View today's menu",
            7 to "Route and vehicle information",
            8 to "Child's report card",
            9 to "Class time table",
            10 to "School events calendar"
    )

    const val DOWNLOAD_MAIN_DIRECTORY = "eSkool"
    const val DOWNLOAD_SUB_DIRECTORY = "download"
    const val DOCUMENT_DOWNLOAD_STATUS_BROADCAST = "document_download_status_broadcast"
    const val DOCUMENT_DOWNLOAD_STATUS = "document_download_status"
    const val DOCUMENT_DOWNLOAD_ITEM_ID = "document_download_item_id"

    fun convertBytes(size: Long): String {
        val kb = size / 1024.0
        val mb = size / (1024.0 * 1024.0)
        val decMb = DecimalFormat("0.00")
        val decKb = DecimalFormat("0")
        return if (mb > 1) "${decMb.format(mb)} MB"
        else if (kb > 1) "${decKb.format(kb)} KB"
        else "$size Bytes"
    }

    fun getAppVersionName(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "V0.0.0"
        }
    }

    fun getAppVersionCode(context: Context): Int {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            0
        }
    }
}
