package net.rmitsolutions.eskool.widgets

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.filedownload.FileDownloadService
import net.rmitsolutions.eskool.models.FileType
import net.rmitsolutions.eskool.models.Document
import kotlinx.android.synthetic.main.fragment_document_bottom_sheet.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.find
import org.jetbrains.anko.startService

/**
 * Created by Madhu on 17-Jul-2017.
 */
class DocumentDetailFragment : BottomSheetDialogFragment() {
    private var fId: Int = 0
    private var fSubject: String = ""
    private var fDocument: String = ""
    private var fDocumentDesc: String = ""
    private var fFileName = ""
    private var fFileType: FileType = FileType.Nothing
    private var downloadImgSrc = 0

    companion object {
        fun newInstance(document: Document, fileType: FileType): DocumentDetailFragment {
            val fragment = DocumentDetailFragment()
            fragment.arguments = bundleOf(
                    "id" to document.id,
                    "subject" to document.subject,
                    "document" to document.documentName,
                    "documentDesc" to document.description,
                    "fileName" to document.docPath,
                    "fileType" to fileType.toString(),
                    "downloadImgSrc" to document.imgSrc
            )
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        fId = bundle!!.getInt("id")
        fSubject = bundle.getString("subject")
        fDocument = bundle.getString("document")
        fDocumentDesc = bundle.getString("documentDesc")
        fFileName = bundle.getString("fileName")
        fFileType = FileType.valueOf(bundle.getString("fileType"))
        downloadImgSrc = bundle.getInt("downloadImgSrc")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_document_bottom_sheet, container, false)
        if (fFileName.isNullOrBlank() || downloadImgSrc == R.drawable.ic_check) {
            val download = view?.find<Button>(R.id.downloadDocument)
            download?.isEnabled = false
            download?.setTextColor(context?.resources!!.getColor(R.color.light_gray))
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        documentSubjectDetail.text = fSubject
        documentNameDetails.text = fDocument
        documentDescriptionDetails.text = fDocumentDesc
        cancelBottomDialog.setOnClickListener { dismiss() }
        downloadDocument.setOnClickListener {
            context?.startService<FileDownloadService>(
                    "id" to fId,
                    "fileName" to fFileName,
                    "fileType" to fFileType
            )
            dismiss()
        }
    }
}