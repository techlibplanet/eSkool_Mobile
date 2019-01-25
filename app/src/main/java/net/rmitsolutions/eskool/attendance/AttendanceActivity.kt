package net.rmitsolutions.eskool.attendance

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateFormat
import android.view.View
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.activity_attendance.*
import kotlinx.android.synthetic.main.include_calendar_appbar_layout.*
import net.rmitsolutions.eskool.CalendarBaseActivity
import net.rmitsolutions.eskool.ESkoolApplication
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.dependency.components.DaggerBaseActivityComponent
import net.rmitsolutions.eskool.helpers.apiAccessToken
import net.rmitsolutions.eskool.helpers.hideProgress
import net.rmitsolutions.eskool.helpers.processRequest
import net.rmitsolutions.eskool.helpers.showProgress
import net.rmitsolutions.eskool.models.AttendanceParams
import net.rmitsolutions.eskool.models.AttendanceViewModel
import net.rmitsolutions.eskool.network.IAttendance
import java.util.*
import javax.inject.Inject

/**
 * Created by Madhu on 19-Aug-17.
 */
class AttendanceActivity : CalendarBaseActivity() {
    @Inject
    lateinit var attendanceService: IAttendance
    private var colorPresent = 0
    private var colorAbsent = 0
    private val adapter: AttendanceAdapter by lazy { AttendanceAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        Handler().post { titleView.text = getString(R.string.dashboard_module_attendance) }

        val depComponent = DaggerBaseActivityComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        depComponent.injectAttendanceActivity(this)

        attendanceRecyclerView.layoutManager = LinearLayoutManager(this)
        attendanceRecyclerView.adapter = adapter

        colorPresent = resources.getColor(R.color.green)
        colorAbsent = resources.getColor(R.color.red)
    }

    override fun onStart() {
        super.onStart()
        getAttendance(Date())
    }

    private fun getAttendance(date: Date) {
        showProgress()
        compactCalendarView.removeAllEvents()
        adapter.items = emptyList()
        adapter.notifyDataSetChanged()
        val cal = Calendar.getInstance()
        cal.time = date
        val data = AttendanceParams(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR))

        compositeDisposable.add(attendanceService.getAttendance(apiAccessToken, data)
                .processRequest(
                        { result ->
                            hideProgress()

                            val attendance = result.list
                            presentCount.text = attendance.sumBy { it.present }.toString()
                            absentCount.text = attendance.sumBy { it.absent }.toString()

                            val list = mutableListOf<AttendanceViewModel>()
                            //get present items
                            attendance.filter { it.present == 1 }.forEach {
                                list.add(AttendanceViewModel(
                                        DateFormat.format("dd", it.day).toString(),
                                        DateFormat.format("EE", it.day).toString(), "", ""))
                            }

                            //get absent items and update in the list.
                            attendance.filter { it.absent == 1 }.forEachIndexed { index, att ->
                                if (list.lastIndex == -1 || index > list.lastIndex) {
                                    list.add(AttendanceViewModel("", "",
                                            DateFormat.format("dd", att.day).toString(),
                                            DateFormat.format("EE", att.day).toString()))
                                } else {
                                    list[index].dateAbsent = DateFormat.format("dd", att.day).toString()
                                    list[index].dayAbsent = DateFormat.format("EE", att.day).toString()
                                }
                            }

                            attendanceContainer.visibility = View.VISIBLE
                            adapter.items = list
                            adapter.notifyDataSetChanged()
                            compactCalendarView.addEvents(
                                    attendance.map { Event(if (it.present == 1) colorPresent else colorAbsent, it.day.time) }
                            )
                        },
                        { message ->
                            attendanceContainer.visibility = View.INVISIBLE
                            hideProgress(true, message)
                        }
                )
        )
    }


    override fun onDateSelected(selectedDate: Date) {
        // nothing to do here
    }

    override fun onMonthSelected(firstDayOfNewMonth: Date) {
        getAttendance(firstDayOfNewMonth)
    }
}