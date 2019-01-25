package net.rmitsolutions.eskool.documents

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.databinding.DocumentDataBinding
import net.rmitsolutions.eskool.filedownload.FileDownloadService
import net.rmitsolutions.eskool.helpers.AutoUpdatableAdapter
import net.rmitsolutions.eskool.helpers.Constants
import net.rmitsolutions.eskool.helpers.isNetConnected
import net.rmitsolutions.eskool.models.Document
import net.rmitsolutions.eskool.models.FileType
import net.rmitsolutions.eskool.widgets.DocumentDetailFragment
import org.jetbrains.anko.startService
import org.jetbrains.anko.toast
import kotlin.properties.Delegates

/**
 * Created by Madhu on 10-Jul-2017.
 */
class DocumentsRecyclerViewAdapter(val fileType: FileType, val fragmentManager: FragmentManager) : RecyclerView.Adapter<DocumentsViewHolder>(), AutoUpdatableAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {


        context = parent.context
        val dataBinding = DocumentDataBinding.inflate(LayoutInflater.from(context), parent, false)
        return DocumentsViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {

        val model = items[position]
        holder.dataBinding.documentDownloadIcon.isEnabled = !model.docPath.isBlank()
        holder.dataBinding.viewModel = model
        holder.dataBinding.ddClickListener = (object : DocumentDownloadClickListener {
            override fun onDocumentDownloadClick(view: View) {
                if (model.imgSrc == R.drawable.ic_check) return
                val studentClassId = Constants.studentModel?.studentClassId ?: 0
                if (studentClassId == 0) {
                    context.toast("Login is required to download.")
                    return
                }
                if (!context.isNetConnected()) {
                    context.toast(context.getString(R.string.you_are_offline))
                    return
                }

                context.toast("Check notification")
                context.startService<FileDownloadService>(
                        "id" to model.id,
                        "fileName" to model.docPath,
                        "fileType" to fileType,
                        "studentClassId" to studentClassId
                )
            }
        })

        holder.dataBinding.root.setOnClickListener {
            val fragment = DocumentDetailFragment.newInstance(model, fileType)
            fragment.show(fragmentManager, "document_detail_fragment")
        }

        holder.dataBinding.executePendingBindings()

    }

    lateinit var context: Context
    var items: List<Document> by Delegates.observable(emptyList()) {
        prop, old, new ->
        autoNotify(old, new) { o, n -> o.id == n.id }
    }

    override fun getItemCount() = items.size





}