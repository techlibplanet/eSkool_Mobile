package net.rmitsolutions.eskool.dependency.components

import android.content.Context
import dagger.Component
import net.rmitsolutions.eskool.database.eSkoolDatabase
import net.rmitsolutions.eskool.dependency.modules.AppContextModule
import net.rmitsolutions.eskool.dependency.modules.DatabaseModule
import net.rmitsolutions.eskool.dependency.modules.NetworkApiModule
import net.rmitsolutions.eskool.dependency.qualifiers.ApplicationContextQualifier
import net.rmitsolutions.eskool.dependency.scopes.ApplicationScope
import net.rmitsolutions.eskool.network.*
import okhttp3.OkHttpClient


/**
 * Created by Madhu on 19-Jun-2017.
 */
@ApplicationScope
@Component(modules = arrayOf(AppContextModule::class, NetworkApiModule::class, DatabaseModule::class))
interface ApplicationComponent {
    @ApplicationContextQualifier
    fun getAppContext(): Context

    fun getOkHttpClient(): OkHttpClient
    fun getDatabase(): eSkoolDatabase

    fun getStudentService(): IStudent
    fun getDocumentsService(): IDocuments
    fun getFileDownloadService(): IFileDownload
    fun getLibraryService(): ILibrary
    fun getCafeteriaService(): ICafeteria
    fun getTransportService(): ITransport
    fun getFeeService(): IFee
    fun getMasterDataService(): IMasterData
    fun getTimeTableService(): ITimeTable
    fun getCalendarService(): ICalendar
    fun getChangePasswordService(): IChangePassword
    fun getAttendanceService(): IAttendance
    fun getArtistData(): IInfoSliders
    fun getForgotPasswordService(): IForgotPassword
    fun getVersionsData():IAssessments
}