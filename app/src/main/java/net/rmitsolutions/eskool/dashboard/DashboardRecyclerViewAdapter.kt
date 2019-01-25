package net.rmitsolutions.eskool.dashboard

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.rmitsolutions.eskool.ModuleActivity
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.attendance.AttendanceActivity
import net.rmitsolutions.eskool.cafeteria.CafeteriaActivity
import net.rmitsolutions.eskool.calendar.CalendarActivity
import net.rmitsolutions.eskool.databinding.DashboardDataBinding
import net.rmitsolutions.eskool.documents.DocumentsActivity
import net.rmitsolutions.eskool.helpers.Constants
import net.rmitsolutions.eskool.profile.ProfileActivity
import net.rmitsolutions.eskool.viewmodels.DashboardViewModel
import org.jetbrains.anko.startActivity



/**
 * Created by Madhu on 29-Jun-2017.
 */
class DashboardRecyclerViewAdapter internal constructor(val list: List<DashboardViewModel>) : RecyclerView.Adapter<DashBoardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardViewHolder {

        context = parent.context
        val inflater = LayoutInflater.from(context)
        val dataBinding = DashboardDataBinding.inflate(inflater, parent, false)
        return DashBoardViewHolder(dataBinding)

    }

    override fun onBindViewHolder(holder: DashBoardViewHolder, position: Int) {

        val dashBoardModuleData = list[position]
        dashBoardModuleData.moduleName = context.getString(dashBoardModuleData.moduleNameRes)
        dashBoardModuleData.moduleDesc = Constants.dashboardModuleDesc[position] ?: ""
        holder.bind(dashBoardModuleData)
        holder.dataBinding.root.setOnClickListener { start(dashBoardModuleData.moduleNameRes) }
    }

    lateinit var context: Context


    override fun getItemCount(): Int = list.size

    private fun start(module: Int) {
        when (module) {
            R.string.dashboard_module_profile -> context.startActivity<ProfileActivity>()
            R.string.dashboard_module_worksheets,
            R.string.dashboard_module_homework -> context.startActivity<DocumentsActivity>("module" to module)
            R.string.dashboard_module_attendance -> context.startActivity<AttendanceActivity>()
            R.string.dashboard_module_cafeteria -> context.startActivity<CafeteriaActivity>()
            R.string.dashboard_module_calendar -> context.startActivity<CalendarActivity>()
            else -> context.startActivity<ModuleActivity>("module" to module)
        }

    }

}