package net.rmitsolutions.eskool.fee

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.databinding.FeeDataBinding
import net.rmitsolutions.eskool.models.Fee

@Suppress("UNREACHABLE_CODE")
/**
 * Created by Madhu on 02-Aug-2017.
 */
class FeeRecyclerViewAdapter() : RecyclerView.Adapter<FeeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeeViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        context = parent.context
        val dataBinding = FeeDataBinding.inflate(LayoutInflater.from(context), parent, false)
        return FeeViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: FeeViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val model = items[position]
        holder.dataBinding.viewModel = model
        if (model.feeName == context.getString(R.string.totals_title)) {
            holder.dataBinding.feeTitle.setBackgroundColor(context.resources.getColor(R.color.very_light_brown))
        }
    }

    private lateinit var context: Context
    var items = emptyList<Fee>()
    override fun getItemCount() = items.size





}