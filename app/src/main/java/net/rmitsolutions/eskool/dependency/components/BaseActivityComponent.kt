package net.rmitsolutions.eskool.dependency.components

import dagger.Component
import net.rmitsolutions.eskool.BaseActivity
import net.rmitsolutions.eskool.ChangePasswordActivity
import net.rmitsolutions.eskool.Changepassword.ChangePassword
import net.rmitsolutions.eskool.WelcomeActivity
import net.rmitsolutions.eskool.attendance.AttendanceActivity
import net.rmitsolutions.eskool.cafeteria.CafeteriaActivity
import net.rmitsolutions.eskool.calendar.CalendarActivity
import net.rmitsolutions.eskool.contact.ContactActivity
import net.rmitsolutions.eskool.dashboard.DashboardActivity
import net.rmitsolutions.eskool.dependency.scopes.ActivityScope
import net.rmitsolutions.eskool.documents.DocumentsActivity
import net.rmitsolutions.eskool.info.InfoSlidersActivity
import net.rmitsolutions.eskool.network.AuthGlideModule

/**
 * Created by Madhu on 19-Jun-2017.
 */
@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface BaseActivityComponent {
    fun injectBaseActivity(baseActivity: BaseActivity)
    fun injectWelcomeActivity(welcomeActivity: WelcomeActivity)
    fun injectDashboardActivity(activity: DashboardActivity)
    fun injectDocumentsActivity(activity: DocumentsActivity)
    fun injectCafeteriaActivity(activity: CafeteriaActivity)
    fun injectContactActivity(activity: ContactActivity)
    fun injectInfoSlidersActivity(activity: InfoSlidersActivity)
    fun injectCalendarActivity(activity: CalendarActivity)
    fun injectChangePasswordActivity(activity: ChangePasswordActivity)
    fun injectAttendanceActivity(activity: AttendanceActivity)
    fun injectChangePassword(activity: ChangePassword)
    fun injectGlideModule(glideModule: AuthGlideModule)
}