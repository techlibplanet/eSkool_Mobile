package net.rmitsolutions.eskool

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.include_calendar_appbar_layout.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Madhu on 04-Jul-2017.
 */
abstract class CalendarBaseActivity : AppCompatActivity(), CalendarChangedListener {
    lateinit internal var fireBaseAnalytics: FirebaseAnalytics
    lateinit internal var compositeDisposable: CompositeDisposable
    private val dateFormat = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)
    private var isExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()
        fireBaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setToolbar()
        setAppBarLayout()
    }

    private fun setToolbar() {
        toolbarActionbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbarActionbar.setNavigationOnClickListener {
            finishAfterTransition()
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setSupportActionBar(toolbarActionbar)
    }

    private fun setAppBarLayout() {
//        appBarLayout.addOnOffsetChangedListener({ appBarLayout, verticalOffset ->
//            isExpanded = Math.abs(verticalOffset) - appBarLayout.totalScrollRange != 0
//            animateDatePickerArrow(isExpanded)
//        })
        //Todo check this commented code later

        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY)
        compactCalendarView.setShouldDrawDaysHeader(true)
        compactCalendarView.shouldSelectFirstDayOfMonthOnScroll(false)
        compactCalendarView.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                setSubtitle(dateFormat.format(dateClicked))
                onDateSelected(dateClicked)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                setSubtitle(dateFormat.format(firstDayOfNewMonth))
                onMonthSelected(firstDayOfNewMonth)
            }
        })

        setCurrentDate(Date())

        datePickerLayout.setOnClickListener {
            if (isExpanded) {
                appBarLayout.setExpanded(false, true)
                isExpanded = false
            } else {
                appBarLayout.setExpanded(true, true)
                isExpanded = true
            }

            animateDatePickerArrow(isExpanded)
        }
    }

    private fun animateDatePickerArrow(expanded: Boolean) {
        if (expanded) {
            ViewCompat.animate(datePickerArrow).rotation(0f).start()
        } else {
            ViewCompat.animate(datePickerArrow).rotation(180f).start()
        }
    }

    private fun setSubtitle(subtitle: String) {
        datePickerTextView.text = subtitle
    }

    private fun setCurrentDate(date: Date) {
        setSubtitle(dateFormat.format(date))
        compactCalendarView.setCurrentDate(date)
    }
}