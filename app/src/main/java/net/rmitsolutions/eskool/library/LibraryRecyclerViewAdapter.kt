package net.rmitsolutions.eskool.library

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.databinding.LibraryDataBinding
import net.rmitsolutions.eskool.models.Library
import java.util.*


/**
 * Created by Madhu on 24-Jul-2017.
 */
class LibraryRecyclerViewAdapter : RecyclerView.Adapter<LibraryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {


        context = parent.context
        val dataBinding = LibraryDataBinding.inflate(LayoutInflater.from(context), parent, false)
        return LibraryViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {

        val model = items[position]
        if (model.dueDate.time < Date().time) {
            model.imgSrc = R.drawable.ic_library_books_red
        } else {
            model.imgSrc = R.drawable.ic_library_books_green
        }
        holder.dataBinding.viewModel = model
    }

    private lateinit var context: Context
    var items = emptyList<Library>()
    override fun getItemCount() = items.size




}