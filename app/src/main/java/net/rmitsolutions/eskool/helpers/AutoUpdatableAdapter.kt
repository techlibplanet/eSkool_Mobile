package net.rmitsolutions.eskool.helpers

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

/**
 * Created by Madhu on 14-Jul-2017.
 */
interface AutoUpdatableAdapter {
    fun <T, V : RecyclerView.ViewHolder> RecyclerView.Adapter<V>.autoNotify(oldList: List<T>, newList: List<T>, compare: (T, T) -> Boolean) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(oldList[oldItemPosition], newList[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size
        })

        diff.dispatchUpdatesTo(this)
    }
}