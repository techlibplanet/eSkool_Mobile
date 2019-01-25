package net.rmitsolutions.eskool.downloads

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.rmitsolutions.eskool.databinding.DownloadsListDataBinding
import net.rmitsolutions.eskool.helpers.AutoUpdatableAdapter
import net.rmitsolutions.eskool.viewmodels.DownloadsViewModel
import kotlin.properties.Delegates

@Suppress("UNREACHABLE_CODE")
/**
 * Created by Madhu on 19-Jul-2017.
 */
class DownloadsRecyclerViewAdapter(val listener: OnItemClickListener) : RecyclerView.Adapter<DownloadsViewHolder>(), AutoUpdatableAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadsViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        val dataBinding = DownloadsListDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = DownloadsViewHolder(dataBinding)

        viewHolder.dataBinding.ddClickListener = (object : DownloadFileClickListener {
            override fun onDownloadFileOpenClick(view: View) {
                val pos = viewHolder.adapterPosition
                listener.onOpenFileClick(pos)
            }

            override fun onDownloadFileDeleteClick(view: View) {
                val pos = viewHolder.adapterPosition
                listener.onDeleteFileClick(pos)
            }
        })
        return viewHolder

    }

    override fun onBindViewHolder(viewHolder: DownloadsViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val model = items[position]
        viewHolder.dataBinding.viewModel = model
    }

    var items: MutableList<DownloadsViewModel> by Delegates.observable(mutableListOf()) {
        prop, old, new ->
        autoNotify(old, new) { o, n -> o.fileName == n.fileName }
    }

    override fun getItemCount() = items.size


}