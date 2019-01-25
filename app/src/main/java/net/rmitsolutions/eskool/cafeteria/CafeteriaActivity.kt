package net.rmitsolutions.eskool.cafeteria

import android.os.Bundle
import android.os.Handler
import net.rmitsolutions.eskool.CalendarBaseActivity
import net.rmitsolutions.eskool.ESkoolApplication
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.dependency.components.DaggerBaseActivityComponent
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.network.ICafeteria
import kotlinx.android.synthetic.main.activity_cafeteria.*
import kotlinx.android.synthetic.main.include_calendar_appbar_layout.*
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by Madhu on 27-Jul-2017.
 */
class CafeteriaActivity : CalendarBaseActivity() {
    @Inject
    lateinit var cafeteriaService: ICafeteria
    private lateinit var adapter: CafeteriaMenuListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafeteria)
        Handler().post { titleView.text = getString(R.string.dashboard_module_cafeteria) }
        val depComponent = DaggerBaseActivityComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        depComponent.injectCafeteriaActivity(this)

        adapter = CafeteriaMenuListAdapter()
        cafeteriaListView.setAdapter(adapter)
        cafeteriaListView.isNestedScrollingEnabled = true
    }

    override fun onStart() {
        super.onStart()
        onDateSelected(Date())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishNoAnim()
    }

    private fun getCafeteriaMenu(day: String) {
        if (!isNetConnected()) return
        showProgress()
        compositeDisposable.add(cafeteriaService.getCafeteriaMenu(apiAccessToken, day)
                .processRequest(
                        { menu ->
                            hideProgress()
                            adapter.items = menu.menuList
                            for (i in 0..adapter.groupCount - 1) {
                                cafeteriaListView.expandGroup(i)
                            }
                            adapter.notifyDataSetChanged()
                        },
                        { err ->
                            hideProgress(true, err)
                            adapter.items = emptyList()
                            adapter.notifyDataSetChanged()
                        }
                )
        )
    }

    override fun onDateSelected(selectedDate: Date) {
        val day = DateFormat.getDateInstance().format(selectedDate)
        if (dayTitle.text == day) {
            return
        }
        dayTitle.text = day
        getCafeteriaMenu(day)
    }

    override fun onMonthSelected(firstDayOfNewMonth: Date) {
        // nothing to do here
    }
}