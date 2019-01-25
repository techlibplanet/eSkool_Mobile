package net.rmitsolutions.eskool.profile

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.rmitsolutions.eskool.databinding.ProfileDataBinding
import net.rmitsolutions.eskool.helpers.AutoUpdatableAdapter
import net.rmitsolutions.eskool.viewmodels.ProfileViewModel


/**
 * Created by Madhu on 05-Jul-2017.
 */
class ProfileRecyclerViewAdapter() : RecyclerView.Adapter<ProfileViewHolder>(), AutoUpdatableAdapter {
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        holder.dataBinding.viewModel = items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {

        val dataBinding = ProfileDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(dataBinding)
    }

    var items: List<ProfileViewModel> = emptyList()



    override fun getItemCount() = items.size


}