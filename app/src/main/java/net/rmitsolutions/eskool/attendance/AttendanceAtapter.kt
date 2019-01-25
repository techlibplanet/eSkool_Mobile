package net.rmitsolutions.eskool.attendance

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.rmitsolutions.eskool.databinding.AttendanceDataBinding
import net.rmitsolutions.eskool.models.AttendanceViewModel

/**
 * Created by Madhu on 21-Aug-17.
 */
class AttendanceAdapter : RecyclerView.Adapter<AttendanceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {

        context = parent.context
        val dataBinding = AttendanceDataBinding.inflate(LayoutInflater.from(context), parent, false)
        return AttendanceViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {

        val model = items[position]
        holder.dataBinding.viewModel = model
    }

    private lateinit var context: Context
    var items = emptyList<AttendanceViewModel>()

    override fun getItemCount() = items.size


}