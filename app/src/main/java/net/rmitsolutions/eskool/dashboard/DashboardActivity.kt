package net.rmitsolutions.eskool.dashboard

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import net.rmitsolutions.eskool.BaseActivity
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.viewmodels.DashboardViewModel
import org.jetbrains.anko.find


/**
 * Created by Madhu on 09-Jun-2017.
 */
class DashboardActivity : BaseActivity() {
    /* @Inject
     lateinit var studentService: IStudent*/
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        /*val depComponent = DaggerBaseActivityComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()

        depComponent.injectDashboardActivity(this)*/

        recyclerView = find<RecyclerView>(R.id.dashboard_recycler_view)
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true);
        setDashboardModules()



    }



    override fun getSelfNavDrawerItem() = R.id.nav_reqel_board

    private fun setDashboardModules() {
        val context = this
        val list = mutableListOf<DashboardViewModel>()
        list.add(DashboardViewModel(R.string.dashboard_module_profile, R.drawable.ic_user_account_blue))
        list.add(DashboardViewModel(R.string.dashboard_module_attendance, R.drawable.ic_format_list_checks))
        //list.add(DashboardViewModel(R.string.dashboard_module_fee, R.drawable.ic_currency_inr))
        list.add(DashboardViewModel(R.string.dashboard_module_worksheets, R.drawable.ic_file_word))
        list.add(DashboardViewModel(R.string.dashboard_module_homework, R.drawable.ic_file_document))
        list.add(DashboardViewModel(R.string.dashboard_module_circulars, R.drawable.ic_file_outline))
        list.add(DashboardViewModel(R.string.dashboard_module_library, R.drawable.ic_library))
        list.add(DashboardViewModel(R.string.dashboard_module_cafeteria, R.drawable.ic_food_fork_drink))
        list.add(DashboardViewModel(R.string.dashboard_module_transport, R.drawable.ic_bus_school))
        list.add(DashboardViewModel(R.string.dashboard_module_assessments, R.drawable.ic_chart_pie))
        list.add(DashboardViewModel(R.string.dashboard_module_timetable, R.drawable.ic_timetable))
        list.add(DashboardViewModel(R.string.dashboard_module_calendar, R.drawable.ic_calendar))

        recyclerView.adapter = DashboardRecyclerViewAdapter(list)
    }

}