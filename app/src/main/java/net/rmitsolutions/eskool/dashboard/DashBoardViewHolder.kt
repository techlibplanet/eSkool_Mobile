package net.rmitsolutions.eskool.dashboard

import android.support.v7.widget.RecyclerView
import net.rmitsolutions.eskool.databinding.DashboardDataBinding
import net.rmitsolutions.eskool.viewmodels.DashboardViewModel

/**
 * Created by Madhu on 29-Jun-2017.
 */
class DashBoardViewHolder internal constructor(val dataBinding: DashboardDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {

    fun bind(model: DashboardViewModel) {
        dataBinding.dashBoardViewModel = model
        dataBinding.executePendingBindings()
    }
}
