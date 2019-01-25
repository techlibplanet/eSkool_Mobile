package net.rmitsolutions.eskool.calendar

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.gson.Gson
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.include_calendar_appbar_layout.*
import net.rmitsolutions.eskool.CalendarBaseActivity
import net.rmitsolutions.eskool.ESkoolApplication
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.database.eSkoolDatabase
import net.rmitsolutions.eskool.dependency.components.DaggerBaseActivityComponent
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.models.CalendarParams
import net.rmitsolutions.eskool.models.CalenderEventEntity
import net.rmitsolutions.eskool.models.EventData
import net.rmitsolutions.eskool.network.ICalendar
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

/**
 * Created by Madhu on 01-Aug-2017.
 */
class CalendarActivity : CalendarBaseActivity() {
    @Inject
    lateinit var calendarService: ICalendar
    @Inject
    lateinit var database: eSkoolDatabase
    var curresntSelectedMonth:Date=Date();

    private val adapter: CalendarAdapter by lazy { CalendarAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val depComponent = DaggerBaseActivityComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()

        depComponent.injectCalendarActivity(this)

        Handler().post { titleView.text = getString(R.string.dashboard_module_calendar) }
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true)

        calendarEventsRecyclerView.layoutManager = LinearLayoutManager(this)
        calendarEventsRecyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
        // getEvents(cal.time)
        getCalenderEventsLocalDB(cal.time)

    }

    fun onRefreshClick(view: View) {

        getEvents(curresntSelectedMonth)
        //getCalenderEventsLocalDB(cal.time)
    }

    private fun getEvents(firstDayOfMonth: Date) {
        showProgress()
        if (!internetCheck()) return
        val cal = Calendar.getInstance()
        cal.time = firstDayOfMonth
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))

        compactCalendarView.removeAllEvents()
        adapter.items = emptyList()
        adapter.notifyDataSetChanged()
        val eventColor = resources.getColor(R.color.light_green_shade)

        val data = CalendarParams(firstDayOfMonth, cal.time)
        val fson=Gson().toJson(data);

        compositeDisposable.add(calendarService.getEvents(apiAccessToken, data)
                .processRequest(
                        { result ->
                            hideProgress()

                            val events = result.list
                            adapter.items = events
                            val fson=Gson().toJson(events);
                            Log.d("jsonDATA",""+result)
                            logD(fson)
                            saveAllEventsIntoLocalDb(events,data)

                            adapter.notifyDataSetChanged()

                            compactCalendarView.addEvents(
                                    events.map { Event(eventColor, it.eventDate.time) }
                            )
                        },
                        { message ->
                            hideProgress(true, message)
                        }
                )
        )
    }

    private fun internetCheck(): Boolean {
        if (!isNetConnected(false)) {
            hideProgress(true, "You are Offline")
            calendarEventsRecyclerView.visibility = View.GONE
            return false
        }

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishNoAnim()
    }

    override fun onDateSelected(selectedDate: Date) {
        //nothing to do here
    }

    override fun onMonthSelected(firstDayOfNewMonth: Date) {
        //getEvents(firstDayOfNewMonth)
        curresntSelectedMonth=firstDayOfNewMonth
        getCalenderEventsLocalDB(firstDayOfNewMonth)
        calendarEventsRecyclerView.visibility = View.VISIBLE
    }

    fun saveAllEventsIntoLocalDb(events:List<net.rmitsolutions.eskool.models.Event>,calederParams:CalendarParams){
        //var calenderEvents=ArrayList<CalenderEventEntity>();
        compositeDisposable.add(Single.fromCallable {
            //  database.runInTransaction(new Runnable)
            database.runInTransaction {
                database.calenderEventDao().deleteEvents(calederParams.from.time,calederParams.to.time)
                for (event in events){
                    // var calenderEvent=CalenderEventEntity();

                    var eventDataList=event.events;
                    for (eventData in eventDataList){
                        var calenderEvent=CalenderEventEntity(System.nanoTime(),event.eventDate,eventData.description,eventData.time,eventData.color)
                        logD("Saving"+calenderEvent.id)
                        database.calenderEventDao().insertCalenderEvent(calenderEvent);


                    }
                }
            }
        }.processRequest())



    }

    private fun getCalenderEventsLocalDB(firstDayOfMonth: Date) {
        val eventColor = resources.getColor(R.color.light_green_shade)
        val cal = Calendar.getInstance()
        cal.time = firstDayOfMonth
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        showProgress()
        compositeDisposable.add(
                Single.fromCallable {
                    database.calenderEventDao().getAllCalederEvents(firstDayOfMonth.time,cal.time.time)
                }.processRequest(
                        { calenderEvents ->
                            hideProgress()
                            if (calenderEvents == null||calenderEvents.isEmpty()){

                                getEvents(firstDayOfMonth)
                            }
                            else {

                                var map=HashMap<Date,MutableList<EventData>>()
                                for (calenderEvent in calenderEvents){
                                    logD(calenderEvent.eventDate.toString())
                                    var evenDataList:MutableList<EventData>? = map.get(calenderEvent.eventDate)
                                    if(evenDataList==null){
                                        evenDataList=ArrayList<EventData>()
                                        map.put(calenderEvent.eventDate,evenDataList)
                                    }
                                    var eventData=EventData(calenderEvent.description,calenderEvent.time,calenderEvent.color);
                                    evenDataList.add(eventData);
                                }
                                val events:MutableList<net.rmitsolutions.eskool.models.Event>   =  ArrayList<net.rmitsolutions.eskool.models.Event>()
                                for (entry in map){
                                    var event=net.rmitsolutions.eskool.models.Event(entry.key, entry.value as ArrayList<EventData>);
                                    events.add(event)

                                }
                                var dataJson= Gson().toJson(events)
                                logD(dataJson)
                                adapter.items = events

                                adapter.notifyDataSetChanged()

                                compactCalendarView.addEvents(
                                        events.map { Event(eventColor, it.eventDate.time) }
                                )

                            }
                        },
                        {
                            val cal = Calendar.getInstance()
                            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
                            getEvents(cal.time)
                        }
                )
        )
    }
}